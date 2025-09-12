# Test script to demonstrate completion date formatting
$startTime = Get-Date

Write-Host "===========================" -ForegroundColor Cyan
Write-Host "🗓️ COMPLETION DATE TEST" -ForegroundColor Cyan
Write-Host "Started: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Cyan
Write-Host "===========================" -ForegroundColor Cyan

# Simulate migration execution
$migrationFiles = @("V1__Initial_schema.sql", "V4__Tasks.sql")

foreach ($file in $migrationFiles) {
    $migrationStart = Get-Date
    Write-Host "`n▶ Executing: $file" -ForegroundColor Yellow
    Write-Host "   Start: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

    # Simulate execution time
    Start-Sleep -Milliseconds 50

    $migrationEnd = Get-Date
    $duration = $migrationEnd - $migrationStart

    Write-Host "✅ COMPLETED: $file" -ForegroundColor Green
    Write-Host "   Duration: $($duration.TotalSeconds.ToString("0.00")) seconds" -ForegroundColor Green
    Write-Host "   Completed: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Green
}

$endTime = Get-Date
$totalDuration = $endTime - $startTime

Write-Host "`n===========================" -ForegroundColor Cyan
Write-Host "📊 TEST SUMMARY" -ForegroundColor Cyan
Write-Host "===========================" -ForegroundColor Cyan
Write-Host "Total Duration: $($totalDuration.TotalSeconds.ToString("0.00")) seconds" -ForegroundColor White
Write-Host "Completed: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor White
Write-Host "✅ COMPLETION DATE FORMAT WORKING!" -ForegroundColor Green