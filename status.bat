@echo off
echo ========================================
echo   SIM SEKOLAH - SERVICE STATUS
echo ========================================
echo.

echo Checking service status...
echo.

REM Check Backend (Spring Boot)
echo [1/3] Backend Status:
netstat -ano | findstr :8080 >nul 2>&1
if errorlevel 1 (
    echo   ❌ Backend (Port 8080): STOPPED
) else (
    echo   ✅ Backend (Port 8080): RUNNING
    for /f "tokens=5" %%i in ('netstat -ano ^| findstr :8080') do (
        echo     Process ID: %%i
    )
)

echo.

REM Check Frontend (Express.js)
echo [2/3] Frontend Status:
netstat -ano | findstr :3000 >nul 2>&1
if errorlevel 1 (
    echo   ❌ Frontend (Port 3000): STOPPED
) else (
    echo   ✅ Frontend (Port 3000): RUNNING
    for /f "tokens=5" %%i in ('netstat -ano ^| findstr :3000') do (
        echo     Process ID: %%i
    )
)

echo.

REM Check Database (PostgreSQL/MySQL)
echo [3/3] Database Status:
netstat -ano | findstr :5432 >nul 2>&1
if errorlevel 1 (
    echo   ❌ Database (Port 5432): STOPPED
) else (
    echo   ✅ Database (Port 5432): RUNNING
)

echo.
echo ========================================
echo   ACCESS URLs (if running)
echo ========================================
echo.
echo Backend API:     http://localhost:8080
echo Frontend Web:    http://localhost:3000
echo API Docs:        http://localhost:8080/swagger-ui.html
echo Database:        localhost:5432
echo.
echo Press any key to exit...
pause >nul