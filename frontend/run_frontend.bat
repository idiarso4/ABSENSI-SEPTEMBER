@echo off
REM Script to run frontend with basic checks
setlocal

REM Ensure running from this script's directory
cd /d %~dp0

echo ====================================
echo [Frontend] Environment Debug
echo FRONTEND_DIR: %~dp0
echo PATH:
echo %PATH%
echo ====================================
echo [Frontend] Versions
echo - node -v
node -v
echo - npm -v
npm -v
echo ====================================

REM Check npm availability
where npm >nul 2>&1
if errorlevel 1 (
    echo npm tidak ditemukan. Pastikan Node.js sudah terpasang dan npm ada di PATH.
    echo Unduh dari https://nodejs.org/ lalu buka ulang terminal.
    goto :end
)

REM Install dependencies if node_modules missing
if not exist "node_modules" (
    echo node_modules tidak ditemukan. Menjalankan 'npm install'...
    call npm install || (
        echo Gagal menjalankan 'npm install'. Periksa koneksi internet/izin akses.
        goto :end
    )
)

echo Menjalankan frontend: npm run dev
npm run dev

:end
endlocal
