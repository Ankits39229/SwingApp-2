@echo off
echo Browser Troubleshooting Script
echo =======================================
echo 1. Checking Network Connectivity...
ping www.google.com
echo.
if errorlevel 1 (
    echo Network is not connected. Please check your connection.
) else (
    echo Network is connected.
)

echo 2. Clearing Browser Cache...
echo =======================================
echo Clearing Chrome Cache...
del /f /s /q "%LocalAppData%\Google\Chrome\User Data\Default\Cache\*"
echo Chrome cache cleared.

echo Clearing Firefox Cache...
rd /s /q "%LocalAppData%\Mozilla\Firefox\Profiles"
echo Firefox cache cleared.

echo Clearing Edge Cache...
del /f /s /q "%LocalAppData%\Microsoft\Edge\User Data\Default\Cache\*"
echo Edge cache cleared.

echo 3. Resetting TCP/IP Stack...
echo =======================================
netsh int ip reset
echo TCP/IP stack reset complete.

echo 4. Checking for Browser Updates...
echo =======================================
echo Checking for Chrome Updates...
start chrome --check-for-update
echo Checking for Firefox Updates...
start firefox --update
echo Checking for Edge Updates...
start msedge --update

echo.
echo Troubleshooting Complete.
pause
