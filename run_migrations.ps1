$PGPASSWORD = "postgres"
$DBNAME = "hrmdb"
$DBUSER = "postgres"
$PSQL_PATH = "C:\Program Files\PostgreSQL\17\bin\psql.exe"

$migrationFiles = @(
    "V1__Initial_schema.sql",
    "V2__Seed_initial_data.sql",
    "V2_1__Operational_tables.sql",
    "V2_2__Employee_Benefits_Deductions.sql",
    "V3__Seed_demo_operational_data.sql",
    "V4__Tasks.sql",
    "V4_1__Seed_tasks.sql",
    "V5__Award.sql",
    "V6__Holiday.sql",
    "V7__Notice.sql",
    "V8__Comment.sql",
    "V9__Expense_Category.sql",
    "V10__Employee_Expense.sql",
    "V11__Loan_Type.sql",
    "V12__Employee_Loan.sql",
    "V13__Loan_Repayment.sql",
    "V14__Work_Hours.sql",
    "V15__Overtime_Audit_Fields.sql",
    "V17__Add_Employee_Banking_Fields.sql",
    "V18__Add_Address_Emergency_Contact.sql"
)


$completedMigrations = @()
$failedMigrations = @()
$startTime = Get-Date

Write-Host "===========================" -ForegroundColor Cyan
Write-Host "🗄️ HRM DATABASE MIGRATION" -ForegroundColor Cyan
Write-Host "Started: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Cyan
Write-Host "===========================" -ForegroundColor Cyan

foreach ($file in $migrationFiles) {
    $filePath = "backend\src\main\resources\db\migration\$file"
    $migrationStart = Get-Date

    Write-Host "`n▶ Executing: $file" -ForegroundColor Yellow
    Write-Host "   Path: $filePath" -ForegroundColor Gray
    Write-Host "   Start: $(Get-Date -Format 'HH:mm:ss')" -ForegroundColor Gray

    if (Test-Path $filePath) {
        & $PSQL_PATH -U $DBUSER -d $DBNAME -f $filePath
        $migrationEnd = Get-Date
        $duration = $migrationEnd - $migrationStart

        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ SUCCESS: $file completed" -ForegroundColor Green
            Write-Host "   Duration: $($duration.TotalSeconds.ToString("0.00")) seconds" -ForegroundColor Green
            Write-Host "   Completed: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Green
            $completedMigrations += @{
                File = $file
                CompletedAt = (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
                Duration = "$($duration.TotalSeconds.ToString("0.00"))s"
                Status = "SUCCESS"
            }
        } else {
            Write-Host "❌ FAILED: $file" -ForegroundColor Red
            Write-Host "   Exit Code: $LASTEXITCODE" -ForegroundColor Red
            Write-Host "   Failed: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Red
            $failedMigrations += @{
                File = $file
                FailedAt = (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
                ExitCode = $LASTEXITCODE
                Status = "FAILED"
            }
        }
    } else {
        Write-Host "⚠️ FILE NOT FOUND: $filePath" -ForegroundColor Red
        Write-Host "   Skipped: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Red
        $failedMigrations += @{
            File = $file
            FailedAt = (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
            ExitCode = "FILE_NOT_FOUND"
            Status = "SKIPPED"
        }
    }
}

$endTime = Get-Date
$totalDuration = $endTime - $startTime

Write-Host "`n===========================" -ForegroundColor Cyan
Write-Host "📊 MIGRATION SUMMARY" -ForegroundColor Cyan
Write-Host "===========================" -ForegroundColor Cyan
Write-Host "Total Migrations: $($migrationFiles.Count)" -ForegroundColor White
Write-Host "Successful: $($completedMigrations.Count)" -ForegroundColor Green
Write-Host "Failed: $($failedMigrations.Count)" -ForegroundColor Red
Write-Host "Total Duration: $($totalDuration.TotalSeconds.ToString("0.00")) seconds" -ForegroundColor White
Write-Host "Completed: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor White

if ($completedMigrations.Count -gt 0) {
    Write-Host "`n✅ COMPLETED MIGRATIONS:" -ForegroundColor Green
    foreach ($migration in $completedMigrations) {
        Write-Host "   ✓ $($migration.File) - $($migration.CompletedAt) ($($migration.Duration))" -ForegroundColor Green
    }
}

if ($failedMigrations.Count -gt 0) {
    Write-Host "`n❌ FAILED MIGRATIONS:" -ForegroundColor Red
    foreach ($migration in $failedMigrations) {
        Write-Host "   ✗ $($migration.File) - $($migration.FailedAt) (Exit: $($migration.ExitCode))" -ForegroundColor Red
    }
}

Write-Host "`n===========================" -ForegroundColor Cyan
if ($failedMigrations.Count -eq 0) {
    Write-Host "🎉 ALL MIGRATIONS COMPLETED SUCCESSFULLY!" -ForegroundColor Green
} else {
    Write-Host "⚠️ SOME MIGRATIONS FAILED - CHECK LOGS ABOVE" -ForegroundColor Yellow
}
Write-Host "===========================" -ForegroundColor Cyan

Write-Host "All migrations completed!" -ForegroundColor Green