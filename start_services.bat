@echo off
REM Start backend and frontend services from root directory
setlocal enabledelayedexpansion

REM Get the script directory (handles spaces in path correctly)
set "SCRIPT_DIR=%~dp0"
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

echo ========================================
echo Starting HRM Services
echo ========================================
echo Script directory: %SCRIPT_DIR%
echo.

REM Check if backend directory exists
if not exist "%SCRIPT_DIR%\backend" (
    echo ERROR: Backend directory not found at: %SCRIPT_DIR%\backend
    echo Please ensure the backend directory exists.
    goto :error
)

REM Check if frontend directory exists
if not exist "%SCRIPT_DIR%\frontend" (
    echo ERROR: Frontend directory not found at: %SCRIPT_DIR%\frontend
    echo Please ensure the frontend directory exists.
    goto :error
)

REM Check if Maven exists
if not exist "%SCRIPT_DIR%\backend\maven-portable\apache-maven-3.8.8\bin\mvn.cmd" (
    echo ERROR: Maven not found at: %SCRIPT_DIR%\backend\maven-portable\apache-maven-3.8.8\bin\mvn.cmd
    echo Please ensure Maven is properly installed.
    goto :error
)

REM Check if package.json exists
if not exist "%SCRIPT_DIR%\frontend\package.json" (
    echo ERROR: package.json not found at: %SCRIPT_DIR%\frontend\package.json
    echo Please ensure the frontend dependencies are properly set up.
    goto :error
)

echo Starting Backend Server...
start "Backend Server" cmd /k "pushd "%SCRIPT_DIR%\backend" && echo Current directory: !cd! && call maven-portable\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting Frontend Server...
start "Frontend Server" cmd /k "pushd "%SCRIPT_DIR%\frontend" && echo Current directory: !cd! && npm run dev"

echo.
echo ========================================
echo Services are starting...
echo ========================================
echo.
echo Status:
echo - Backend window: "Backend Server"
echo - Frontend window: "Frontend Server"
echo.
echo Expected results:
echo - Backend: http://localhost:8080 (Spring Boot)
echo - Frontend: http://localhost:3000 (Node.js/Express)
echo.
echo Troubleshooting:
echo - Check the console windows for any error messages
echo - Ensure ports 8080 and 3000 are not in use
echo - Verify PostgreSQL is running (for production)
echo.
echo Press any key to exit this launcher...
pause >nul
goto :end

:error
echo.
echo ========================================
echo SERVICE STARTUP FAILED
echo ========================================
echo Please check the error messages above and resolve the issues.
echo.
pause
exit /b 1

:end
endlocal
exit /b 0
