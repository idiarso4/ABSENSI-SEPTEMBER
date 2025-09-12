# PowerShell script to start backend and frontend services
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting HRM Services" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

# Get the script directory (equivalent to %~dp0 in batch)
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Write-Host "Script directory: $scriptPath" -ForegroundColor Yellow
Write-Host ""

# Backend and Frontend paths
$backendPath = Join-Path $scriptPath "backend"
$frontendPath = Join-Path $scriptPath "frontend"

# Validation checks
$validationErrors = @()

if (-not (Test-Path $backendPath)) {
    $validationErrors += "Backend directory not found at: $backendPath"
}

if (-not (Test-Path $frontendPath)) {
    $validationErrors += "Frontend directory not found at: $frontendPath"
}

$mavenPath = Join-Path $backendPath "maven-portable\apache-maven-3.8.8\bin\mvn.cmd"
if (-not (Test-Path $mavenPath)) {
    $validationErrors += "Maven not found at: $mavenPath"
}

$packageJsonPath = Join-Path $frontendPath "package.json"
if (-not (Test-Path $packageJsonPath)) {
    $validationErrors += "package.json not found at: $packageJsonPath"
}

if ($validationErrors.Count -gt 0) {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "SERVICE STARTUP FAILED" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    foreach ($err in $validationErrors) {
        Write-Host "ERROR: $err" -ForegroundColor Red
    }
    Write-Host ""
    Write-Host "Please resolve the above issues and try again." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "All validation checks passed." -ForegroundColor Green
Write-Host ""

# Start Backend
Write-Host "Starting Backend Server..." -ForegroundColor Cyan
$backendCommand = "pushd `"$backendPath`" && echo Current directory: %cd% && call maven-portable\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run"
Start-Process "cmd.exe" -ArgumentList "/k $backendCommand" -WindowStyle Normal

# Wait before starting frontend
Write-Host "Waiting 3 seconds before starting frontend..." -ForegroundColor Gray
Start-Sleep -Seconds 3

# Start Frontend
Write-Host "Starting Frontend Server..." -ForegroundColor Cyan
$frontendCommand = "pushd `"$frontendPath`" && echo Current directory: %cd% && npm run dev"
Start-Process "cmd.exe" -ArgumentList "/k $frontendCommand" -WindowStyle Normal

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Services are starting..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Status:" -ForegroundColor White
Write-Host "- Backend window: 'Backend Server'" -ForegroundColor White
Write-Host "- Frontend window: 'Frontend Server'" -ForegroundColor White
Write-Host ""
Write-Host "Expected results:" -ForegroundColor White
Write-Host "- Backend: http://localhost:8080 (Spring Boot)" -ForegroundColor White
Write-Host "- Frontend: http://localhost:3000 (Node.js/Express)" -ForegroundColor White
Write-Host ""
Write-Host "Troubleshooting:" -ForegroundColor Yellow
Write-Host "- Check the console windows for any error messages" -ForegroundColor Yellow
Write-Host "- Ensure ports 8080 and 3000 are not in use" -ForegroundColor Yellow
Write-Host "- Verify PostgreSQL is running (for production)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press any key to exit this launcher..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")