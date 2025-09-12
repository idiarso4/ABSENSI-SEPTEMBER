@echo off
echo ========================================
echo   SIM SEKOLAH - HELP & COMMANDS
echo ========================================
echo.
echo Available Commands:
echo.
echo [MAIN COMMANDS - DEVELOPMENT]
echo   start.bat         - 🚀 START SISTEM (Backend + Frontend)
echo   stop.bat          - 🛑 STOP semua services
echo   status.bat        - 📊 Check status services
echo   help.bat          - ❓ Show this help
echo.
echo [DOCKER COMMANDS - PRODUCTION ONLY]
echo   start_docker.bat  - 🐳 Start with Docker (production)
echo   docker-compose up -d     - Start all Docker services
echo   docker-compose down      - Stop all Docker services
echo   docker-compose logs -f   - View real-time logs
echo.
echo [MANUAL STARTUP]
echo   Backend:  cd backend && mvn spring-boot:run
echo   Frontend: cd frontend && npm start
echo   Mobile:   cd mobile && npx react-native run-android
echo.
echo ========================================
echo   ACCESS URLs
echo ========================================
echo.
echo Backend API:     http://localhost:8080
echo Frontend Web:    http://localhost:3000
echo API Docs:        http://localhost:8080/swagger-ui.html
echo Database:        localhost:5432 (PostgreSQL)
echo Redis Cache:     localhost:6379
echo.
echo ========================================
echo   DEFAULT CREDENTIALS
echo ========================================
echo.
echo Admin User:
echo   Email:    admin@simsekolah.com
echo   Password: admin123
echo   Role:     ADMIN
echo.
echo Sample Users:
echo   Teacher:  teacher@simsekolah.com / teacher123
echo   Parent:   parent@simsekolah.com / parent123
echo.
echo ========================================
echo   SYSTEM FEATURES
echo ========================================
echo.
echo ✅ 5 Types of Attendance:
echo    - ARRIVAL_DEPARTURE (face recognition)
echo    - CLASS (teacher-managed)
echo    - PRAYER (religious teacher)
echo    - PERMISSION (duty teacher)
echo    - PKL (supervising teacher)
echo.
echo ✅ 5 User Roles:
echo    - ADMIN, TEACHER, DUTY_TEACHER
echo    - SUPERVISING_TEACHER, PARENT
echo.
echo ✅ GPS & Face Recognition Ready
echo ✅ Academic Management Complete
echo ✅ Real-time Dashboard & Reports
echo ✅ Mobile App Support
echo.
echo ========================================
echo   QUICK TROUBLESHOOTING
echo ========================================
echo.
echo 1. Services not starting?
echo    - Check Java 17+ installed
echo    - Check Node.js 16+ installed
echo    - Check ports 8080, 3000 available
echo.
echo 2. Database connection failed?
echo    - Setup PostgreSQL/MySQL
echo    - Update application.properties
echo    - Run database migrations
echo.
echo 3. Frontend not loading?
echo    - cd frontend && npm install
echo    - Check Node.js version
echo    - Clear browser cache
echo.
echo For detailed troubleshooting: README_SIM_SEKOLAH.md
echo.
pause