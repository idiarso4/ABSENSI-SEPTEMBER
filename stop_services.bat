@echo off
REM Stop backend and frontend services from root directory
setlocal

echo Stopping HRM services...
echo.

REM Get the script directory (handles spaces in path)
set "SCRIPT_DIR=%~dp0"
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

echo Script directory: %SCRIPT_DIR%
echo.

REM Stop backend
echo Stopping Backend Server...
if exist "%SCRIPT_DIR%\backend\stop_backend.bat" (
    pushd "%SCRIPT_DIR%\backend"
    call stop_backend.bat
    popd
    echo Backend stop script executed.
) else (
    echo Backend stop script not found, using manual process termination...
)

echo Terminating Java/Maven processes...

REM Method 1: Kill all Java processes
echo Killing Java processes...
taskkill /f /im java.exe >nul 2>&1
if %errorlevel% equ 0 (
    echo Java processes terminated successfully.
) else (
    echo No Java processes found or already terminated.
)

REM Method 2: Kill Maven processes
echo Killing Maven processes...
taskkill /f /im mvn.cmd >nul 2>&1
taskkill /f /im maven.exe >nul 2>&1

REM Method 3: Kill processes by port 8080 (Spring Boot default)
echo Killing processes on port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    echo Terminating process %%a on port 8080
    taskkill /f /pid %%a >nul 2>&1
)

REM Method 4: Kill by window title
echo Killing Backend Server windows...
taskkill /f /fi "WINDOWTITLE eq Backend Server*" >nul 2>&1

REM Method 5: Force kill any remaining Java processes with Spring Boot in command line
echo Checking for Spring Boot processes...
for /f "tokens=2" %%a in ('wmic process where "name='java.exe' and CommandLine like '%%spring-boot%%'" get ProcessId /value ^| find "="') do (
    echo Terminating Spring Boot process %%a
    taskkill /f /pid %%a >nul 2>&1
)

echo Backend termination completed.
echo.

REM Stop frontend
echo Stopping Frontend Server...
if exist "%SCRIPT_DIR%\frontend\stop_frontend.bat" (
    pushd "%SCRIPT_DIR%\frontend"
    call stop_frontend.bat
    popd
    echo Frontend stop script executed.
) else (
    echo Frontend stop script not found at: %SCRIPT_DIR%\frontend\stop_frontend.bat
    echo Attempting to kill Node.js processes manually...

    REM Kill npm processes
    for /f "tokens=2 delims=," %%a in ('tasklist /fi "imagename eq npm.cmd" /fo csv /nh') do (
        taskkill /PID %%a /F >nul 2>&1
    )

    REM Kill node processes
    for /f "tokens=2 delims=," %%a in ('tasklist /fi "imagename eq node.exe" /fo csv /nh') do (
        taskkill /PID %%a /F >nul 2>&1
    )

    REM Kill processes on port 3000
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000') do (
        taskkill /f /pid %%a >nul 2>&1
    )
)
echo.

REM Kill any remaining service windows
echo Closing service command windows...
for /f "tokens=2 delims=," %%a in ('tasklist /fi "windowtitle eq Backend Server*" /fo csv /nh') do (
    taskkill /PID %%a /F >nul 2>&1
)
for /f "tokens=2 delims=," %%a in ('tasklist /fi "windowtitle eq Frontend Server*" /fo csv /nh') do (
    taskkill /PID %%a /F >nul 2>&1
)

echo.
echo ========================================
echo Services stop command completed.
echo ========================================
echo.
echo Note:
echo - Backend and frontend processes have been terminated
echo - It may take a few seconds for all services to stop completely
echo - All service command windows should be closed
echo - If services are still running, check Task Manager
echo.
echo Press any key to exit...
pause >nul

endlocal