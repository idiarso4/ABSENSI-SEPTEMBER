@echo off
echo ========================================
echo   SIM SEKOLAH - STOP ALL SERVICES
echo ========================================
echo.

echo Stopping SIM Sekolah Services...
echo.

REM Kill Backend Process (Spring Boot)
echo [1/2] Stopping Backend (Spring Boot)...
for /f "tokens=2" %%i in ('tasklist /fi "WINDOWTITLE eq SIM Sekolah Backend" /nh') do (
    if not "%%i"=="No" (
        taskkill /pid %%i /f >nul 2>&1
        if errorlevel 1 (
            echo   - Backend process not found or already stopped
        ) else (
            echo   - Backend stopped successfully
        )
    )
)

REM Kill Frontend Process (Node.js)
echo [2/2] Stopping Frontend (Express.js)...
for /f "tokens=2" %%i in ('tasklist /fi "WINDOWTITLE eq SIM Sekolah Frontend" /nh') do (
    if not "%%i"=="No" (
        taskkill /pid %%i /f >nul 2>&1
        if errorlevel 1 (
            echo   - Frontend process not found or already stopped
        ) else (
            echo   - Frontend stopped successfully
        )
    )
)

REM Kill any remaining Java processes (Spring Boot)
echo [3/3] Cleaning up remaining processes...
taskkill /f /im java.exe >nul 2>&1
taskkill /f /im javaw.exe >nul 2>&1

REM Kill any remaining Node.js processes
taskkill /f /im node.exe >nul 2>&1
taskkill /f /im npm.cmd >nul 2>&1

echo.
echo ========================================
echo   ALL SERVICES STOPPED!
echo ========================================
echo.
echo Services have been stopped successfully.
echo You can now safely close all command windows.
echo.
pause