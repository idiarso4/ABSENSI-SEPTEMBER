# PowerShell script to stop backend and frontend services
Write-Host "Stopping HRM services..." -ForegroundColor Red

# Stop backend processes
Write-Host "Stopping Backend processes..." -ForegroundColor Yellow
try {
    # Method 1: Kill Java processes
    $javaProcesses = Get-Process java -ErrorAction SilentlyContinue
    if ($javaProcesses) {
        $javaProcesses | Stop-Process -Force
        Write-Host "Java processes terminated." -ForegroundColor Green
    } else {
        Write-Host "No Java processes found." -ForegroundColor Gray
    }

    # Method 2: Kill Maven processes
    $mavenProcesses = Get-Process mvn -ErrorAction SilentlyContinue
    if ($mavenProcesses) {
        $mavenProcesses | Stop-Process -Force
        Write-Host "Maven processes terminated." -ForegroundColor Green
    }

    # Method 3: Kill processes on port 8080 (Spring Boot default)
    $port8080Processes = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
    if ($port8080Processes) {
        foreach ($processId in $port8080Processes) {
            Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
        }
        Write-Host "Processes on port 8080 terminated." -ForegroundColor Green
    }

    # Method 4: Kill by Spring Boot command line
    $springProcesses = Get-WmiObject Win32_Process | Where-Object {
        $_.Name -eq 'java.exe' -and $_.CommandLine -like '*spring-boot*'
    }
    if ($springProcesses) {
        foreach ($process in $springProcesses) {
            Stop-Process -Id $process.ProcessId -Force -ErrorAction SilentlyContinue
        }
        Write-Host "Spring Boot processes terminated." -ForegroundColor Green
    }

} catch {
    Write-Host "Error stopping backend processes: $($_.Exception.Message)" -ForegroundColor Red
}

# Stop frontend processes
Write-Host "Stopping Frontend processes..." -ForegroundColor Yellow
try {
    # Kill npm processes
    $npmProcesses = Get-Process npm -ErrorAction SilentlyContinue
    if ($npmProcesses) {
        $npmProcesses | Stop-Process -Force
        Write-Host "NPM processes stopped." -ForegroundColor Green
    }

    # Kill node processes
    $nodeProcesses = Get-Process node -ErrorAction SilentlyContinue
    if ($nodeProcesses) {
        $nodeProcesses | Stop-Process -Force
        Write-Host "Node.js processes stopped." -ForegroundColor Green
    }

    # Kill processes on port 3000
    $port3000Processes = Get-NetTCPConnection -LocalPort 3000 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
    if ($port3000Processes) {
        foreach ($processId in $port3000Processes) {
            Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
        }
        Write-Host "Processes on port 3000 stopped." -ForegroundColor Green
    }
} catch {
    Write-Host "Error stopping frontend processes: $($_.Exception.Message)" -ForegroundColor Red
}

# Stop any remaining cmd windows with specific titles
Write-Host "Stopping service windows..." -ForegroundColor Yellow
try {
    $cmdProcesses = Get-Process cmd -ErrorAction SilentlyContinue | Where-Object {
        $_.MainWindowTitle -like "*Backend*" -or $_.MainWindowTitle -like "*Frontend*"
    }
    if ($cmdProcesses) {
        $cmdProcesses | Stop-Process -Force
        Write-Host "Service command windows closed." -ForegroundColor Green
    }
} catch {
    Write-Host "Error stopping command windows: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Service stop process completed." -ForegroundColor Green
Write-Host "Note: It may take a few seconds for all services to fully terminate." -ForegroundColor White