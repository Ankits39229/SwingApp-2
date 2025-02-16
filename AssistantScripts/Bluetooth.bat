@echo off
cls
echo Bluetooth and Wireless Troubleshooter
echo =======================================
echo.
echo Checking Bluetooth and Wireless Devices...
echo.
echo 1. Restarting Bluetooth Service...
net stop "Bluetooth Support Service" >nul 2>&1
net start "Bluetooth Support Service" >nul 2>&1
echo Bluetooth Service Restarted.
echo.

echo 2. Restarting WLAN AutoConfig Service...
net stop "WLAN AutoConfig" >nul 2>&1
net start "WLAN AutoConfig" >nul 2>&1
echo WLAN AutoConfig Service Restarted.
echo.

echo 3. Checking Bluetooth Adapter Status...
setlocal enabledelayedexpansion
for /f "tokens=3" %%i in ('reg query "HKLM\SYSTEM\CurrentControlSet\Services\BTHPORT\Parameters" /v "ServiceName" 2^>nul') do set BluetoothStatus=%%i
if "%BluetoothStatus%"=="Bluetooth" (
    echo Bluetooth Adapter is enabled.
) else (
    echo Bluetooth Adapter is not enabled or found.
)
echo.

echo 4. Checking Wireless Network Adapter Status...
setlocal enabledelayedexpansion
for /f "tokens=2 delims=," %%i in ('wmic nic where "NetConnectionStatus=2" get Name /format:csv') do set wirelessStatus=%%i
if defined wirelessStatus (
    echo Wireless Network Adapter is enabled.
) else (
    echo Wireless Network Adapter is not enabled or found.
)
echo.

echo 5. Checking for Common Bluetooth Device Errors...
echo ============================================
start ms-settings:troubleshoot
echo Bluetooth Troubleshooter opened.

echo.
echo 6. Checking for Common Wireless Device Errors...
echo ============================================
start ms-settings:troubleshoot
echo Wireless Troubleshooter opened.

echo.
echo Finished troubleshooting.


timeout /t 2 /nobreak >nul
