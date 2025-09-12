@echo off

REM Stop all Java processes
taskkill /f /im javaw.exe /t 2>nul
taskkill /f /im java.exe /t 2>nul

REM Kill all Node/npm processes
taskkill /f /im npm.cmd /t 2>nul
taskkill /f /im node.exe /t 2>nul

REM Start frontend
cd frontend
start cmd /k "npm run dev"

REM Start backend
cd ..
cd backend
start cmd /k "\"C:\Users\sija_003\Desktop\HRM dan Penggajian\backend\maven-portable\apache-maven-3.8.8\bin\mvn.cmd\" spring-boot:run"