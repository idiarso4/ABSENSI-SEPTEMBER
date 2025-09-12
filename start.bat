@echo off
echo ========================================
echo   SIM SEKOLAH - START ALL SERVICES
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17+ and add to PATH
    pause
    exit /b 1
)

REM Check if Node.js is installed
node --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js and add to PATH
    pause
    exit /b 1
)

echo Starting SIM Sekolah Services...
echo.

REM Start Backend Service
echo [1/2] Starting Backend (Spring Boot)...
start "SIM Sekolah Backend" cmd /k "cd backend && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

REM Start Frontend Service
echo [2/2] Starting Frontend (Express.js)...
start "SIM Sekolah Frontend" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo   SERVICES STARTED SUCCESSFULLY!
echo ========================================
echo.
echo Backend API:    http://localhost:8080
echo Frontend Web:   http://localhost:3000
echo API Docs:       http://localhost:8080/swagger-ui.html
echo.
echo Press any key to close this window...
echo (Services will continue running in background)
pause >nul