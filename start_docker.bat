@echo off
echo ========================================
echo   SIM SEKOLAH - DOCKER STARTUP
echo ========================================
echo.

echo Starting SIM Sekolah with Docker Compose...
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker is not installed or not running
    echo Please install Docker Desktop and start Docker service
    echo https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Check if Docker Compose is available
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker Compose is not available
    echo Please ensure Docker Compose is installed
    pause
    exit /b 1
)

echo [1/3] Building Docker images...
docker-compose build

echo.
echo [2/3] Starting services...
docker-compose up -d

echo.
echo [3/3] Waiting for services to be ready...
timeout /t 10 /nobreak >nul

echo.
echo ========================================
echo   SIM SEKOLAH DOCKER STARTED!
echo ========================================
echo.
echo Services Status:
docker-compose ps
echo.
echo Access URLs:
echo Backend API:     http://localhost:8080
echo Frontend Web:    http://localhost:3000
echo API Docs:        http://localhost:8080/swagger-ui.html
echo Database:        localhost:5432 (PostgreSQL)
echo Redis Cache:     localhost:6379
echo.
echo Docker Logs:
echo docker-compose logs -f
echo.
echo To stop services: docker-compose down
echo.
pause