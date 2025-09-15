param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$BearerToken,
    [string]$Username,
    [string]$Password,
    [string]$LoginIdentifier,
    [string]$LoginPassword
)

<#
 .SYNOPSIS
    Quick smoke test for Excel import endpoints using dryRun.

 .DESCRIPTION
    - Downloads the latest Excel templates into the fixtures folder
    - Posts each template back to its corresponding /excel/import endpoint with ?dryRun=true
    - Prints a compact summary (status, totalRows, successfulImports, failedImports, createdItems count, errors count)

 .PARAMETER BaseUrl
    Base URL of the backend. Default: http://localhost:8080

 .EXAMPLE
    .\tools\smoke_imports.ps1 -BaseUrl http://localhost:8080
#>

Add-Type -AssemblyName 'System.Web' -ErrorAction SilentlyContinue

function Download-TemplatesInternal {
    param(
        [array]$Targets,
        [string]$FixturesDir,
        [hashtable]$Headers,
        [string]$BaseUrl
    )

    Write-Host "[STEP] Pulling latest templates …" -ForegroundColor Magenta
    foreach ($t in $Targets) {
        $url = "$BaseUrl$($t.Path)"
        $out = Join-Path $FixturesDir $t.Name
        try {
            Write-Host " -> $($t.Name)"
            Invoke-WebRequest -UseBasicParsing -Uri $url -Headers $Headers -OutFile $out
        } catch {
            Write-Host "  ERROR: $($_.Exception.Message)" -ForegroundColor Red
            throw
        }
    }
}

function Invoke-ImportDryRun {
    param(
        [string]$Name,
        [string]$FilePath,
        [string]$Endpoint,
        [hashtable]$Headers,
        [string]$BaseUrl
    )

    Write-Host "[SMOKE] $Name → $Endpoint" -ForegroundColor Cyan
    if (-not (Test-Path $FilePath)) {
        Write-Host "  File not found: $FilePath" -ForegroundColor Red
        return
    }

    $url = "$BaseUrl$Endpoint"
    $authHeader = ""
    if ($Headers.ContainsKey('Authorization')) {
        $authHeader = "-H `"Authorization: $($Headers['Authorization'])`""
    }

    try {
        # Use curl for multipart/form-data upload
        $tempFile = [System.IO.Path]::GetTempFileName()
        $curlCmd = "& curl.exe -s -X POST '$url' -H 'Authorization: $($Headers['Authorization'])' -F 'file=@`"$FilePath`"`' -F 'dryRun=true' > '$tempFile'"
        Invoke-Expression $curlCmd
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  Request failed: curl exit code $LASTEXITCODE" -ForegroundColor Red
            if (Test-Path $tempFile) { Remove-Item $tempFile -Force }
            return
        }
        $result = Get-Content $tempFile -Raw
        if (Test-Path $tempFile) { Remove-Item $tempFile -Force }
        $json = $result | ConvertFrom-Json
        $createdCount = @($json.createdItems).Count
        $errorsCount = @($json.errors).Count
        Write-Host ("  status={0} totalRows={1} ok={2} failed={3} createdItems={4} errors={5}" -f ($json.status), ($json.totalRows), ($json.successfulImports), ($json.failedImports), $createdCount, $errorsCount) -ForegroundColor Green

        if ($errorsCount -gt 0) {
            $preview = $json.errors | Select-Object -First 3
            Write-Host "  sample errors:" -ForegroundColor Yellow
            $preview | ForEach-Object { Write-Host ("   - row={0} field={1} reason={2}" -f $_.rowNumber, $_.field, $_.message) }
        }
    } catch {
        Write-Host "  Request failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Invoke-Login {
    param(
        [string]$Identifier,
        [string]$Password,
        [string]$BaseUrl
    )

    if (-not $Identifier -or -not $Password) { return $null }
    $loginUrl = "$BaseUrl/api/v1/auth/login"
    $payload = @{ identifier = $Identifier; password = $Password; rememberMe = $false } | ConvertTo-Json -Depth 3
    try {
        Write-Host "[AUTH] Logging in as '$Identifier' …" -ForegroundColor Cyan
        $resp = Invoke-RestMethod -Uri $loginUrl -Method Post -ContentType 'application/json' -Body $payload -ErrorAction Stop
        if ($resp.accessToken) {
            Write-Host "[AUTH] Got access token (type=$($resp.tokenType))" -ForegroundColor Green
            return [string]$resp.accessToken
        }
        Write-Host "[AUTH] Login response did not include accessToken" -ForegroundColor Yellow
        return $null
    } catch {
        Write-Host "[AUTH] Login failed: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

try {
    $root = Split-Path -Parent $PSScriptRoot
    $fixturesDir = Join-Path $root 'fixtures'

    # Acquire token if credentials provided
    $token = $null
    if ($LoginIdentifier -and $LoginPassword) {
        $token = Invoke-Login -Identifier $LoginIdentifier -Password $LoginPassword -BaseUrl $BaseUrl
    } elseif ($BearerToken) {
        $token = $BearerToken
    } elseif ($Username -and $Password) {
        $basicPair = "${Username}:${Password}"
    }

    $headers = @{}
    if ($token) { $headers['Authorization'] = "Bearer $token" }

    # 1) Pull fresh templates (authenticated internal downloader)
    $templateTargets = @(
        @{ Name = "subject_import_template.xlsx";    Path = "/api/v1/subjects/excel/template" },
        @{ Name = "teacher_import_template.xlsx";    Path = "/api/v1/teachers/excel/template" },
        @{ Name = "classrooms_import_template.xlsx"; Path = "/api/v1/classrooms/excel/template" },
        @{ Name = "student_import_template.xlsx";    Path = "/api/v1/students/excel/template" }
    )
    Download-TemplatesInternal -Targets $templateTargets -FixturesDir $fixturesDir -Headers $headers -BaseUrl $BaseUrl

    # 2) Define smoke targets (template file → import endpoint)
    $targets = @(
        @{ Name = 'Subjects';  File = Join-Path $fixturesDir 'subject_import_template.xlsx';     Endpoint = '/api/v1/subjects/excel/import' },
        @{ Name = 'Teachers';  File = Join-Path $fixturesDir 'teacher_import_template.xlsx';     Endpoint = '/api/v1/teachers/excel/import' },
        @{ Name = 'Classrooms';File = Join-Path $fixturesDir 'classrooms_import_template.xlsx';  Endpoint = '/api/v1/classrooms/excel/import' },
        @{ Name = 'Students';  File = Join-Path $fixturesDir 'student_import_template.xlsx';     Endpoint = '/api/v1/students/excel/import' }
    )

    Write-Host "[STEP] Running dryRun imports …" -ForegroundColor Magenta
    foreach ($t in $targets) {
        Invoke-ImportDryRun -Name $t.Name -FilePath $t.File -Endpoint $t.Endpoint -Headers $headers -BaseUrl $BaseUrl
    }

    Write-Host "[DONE] Smoke run complete." -ForegroundColor Cyan
}
catch {
    Write-Host "[FATAL] $_" -ForegroundColor Red
    exit 1
}
