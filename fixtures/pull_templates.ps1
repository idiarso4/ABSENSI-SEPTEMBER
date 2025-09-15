# Downloads latest Excel templates to the fixtures folder for quick editing/testing
param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$fixtures = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "Downloading templates from $BaseUrl ..."

$targets = @(
    @{ Name = "subject_import_template.xlsx"; Path = "/api/v1/subjects/excel/template" },
    @{ Name = "teacher_import_template.xlsx"; Path = "/api/v1/teachers/excel/template" },
    @{ Name = "classrooms_import_template.xlsx"; Path = "/api/v1/classrooms/excel/template" },
    @{ Name = "student_import_template.xlsx"; Path = "/api/v1/students/excel/template" }
)

foreach ($t in $targets) {
    $url = "$BaseUrl$($t.Path)"
    $out = Join-Path $fixtures $t.Name
    Write-Host " -> $($t.Name)"
    Invoke-WebRequest -UseBasicParsing -Uri $url -OutFile $out
}

Write-Host "Done. Files saved to: $fixtures"