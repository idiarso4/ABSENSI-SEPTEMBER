@echo off
REM Script to stop frontend (nodemon/server.js)
setlocal

echo Stopping frontend services...

REM Method 1: Kill npm processes specifically
echo Attempting to kill npm processes...
taskkill /f /im npm.cmd 2>nul
taskkill /f /im npm.exe 2>nul

REM Method 2: Kill node processes specifically
echo Attempting to kill node processes...
taskkill /f /im node.exe 2>nul

REM Method 3: Kill processes by port (if we know the port)
echo Attempting to kill processes on port 3000...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000') do (
    taskkill /f /pid %%a 2>nul
)

echo Frontend services stop command executed.
echo If the frontend is still running, please close the console window manually.
endlocal