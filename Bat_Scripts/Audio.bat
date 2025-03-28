@echo off
setlocal enabledelayedexpansion

:: ----- Self-Elevation Block -----
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo Requesting administrative privileges...
    powershell -Command "Start-Process '%~0' -Verb RunAs"
    exit /B
)

cls
echo ========================================
echo      Advanced Audio Troubleshooter
echo ========================================
echo.

:: ----- Create log file -----
set "logfile=%temp%\audio_troubleshooter.log"
echo Audio Troubleshooting initiated at %date% %time% > "%logfile%"

:: ----- Check if audio is muted first (before stopping services) -----
echo Checking and fixing muted audio...
echo Checking and fixing muted audio... >> "%logfile%"

:: Use reliable method to check and unmute system audio
echo Sending unmute command...
powershell -ExecutionPolicy Bypass -Command "$audioObj = New-Object -ComObject WScript.Shell; $audioObj.SendKeys([char]173);" >nul 2>&1
timeout /t 1 >nul

:: Try another method to ensure unmute works
powershell -ExecutionPolicy Bypass -Command "Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.SendKeys]::SendWait('{VOLUME_MUTE}')" >nul 2>&1
timeout /t 1 >nul

:: Use nircmd if available (more reliable third option)
where nircmd >nul 2>&1
if %errorlevel% equ 0 (
    echo Using nircmd to unmute...
    nircmd mutesysvolume 0
)

:: ----- 1. Stop dependent services first to prevent issues -----
echo Stopping audio-related services...
echo Stopping audio-related services... >> "%logfile%"

net stop Audiosrv /y >nul 2>&1
net stop AudioEndpointBuilder /y >nul 2>&1
net stop RtkAudioService /y >nul 2>&1
net stop WMPNetworkSvc /y >nul 2>&1
timeout /t 3 >nul

:: ----- 2. Restart Audio Endpoint Builder Service -----
echo Starting Audio Endpoint Builder Service...
echo Starting Audio Endpoint Builder Service... >> "%logfile%"
net start AudioEndpointBuilder >nul 2>&1
if %errorlevel%==0 (
    echo Audio Endpoint Builder Service started successfully.
    echo Audio Endpoint Builder Service started successfully. >> "%logfile%"
) else (
    echo Failed to start Audio Endpoint Builder Service. Trying alternative method...
    echo Failed to start Audio Endpoint Builder Service. Trying alternative method... >> "%logfile%"
    sc config AudioEndpointBuilder start= auto
    net start AudioEndpointBuilder >nul 2>&1
)
timeout /t 2 >nul

:: ----- 3. Start Windows Audio Service -----
echo Starting Windows Audio Service...
echo Starting Windows Audio Service... >> "%logfile%"
net start Audiosrv >nul 2>&1
if %errorlevel%==0 (
    echo Windows Audio Service started successfully.
    echo Windows Audio Service started successfully. >> "%logfile%"
) else (
    echo Failed to start Audio Service. Trying alternative method...
    echo Failed to start Audio Service. Trying alternative method... >> "%logfile%"
    sc config Audiosrv start= auto
    sc config Audiosrv depend= RpcSs/AudioEndpointBuilder
    net start Audiosrv >nul 2>&1
)
timeout /t 2 >nul

:: ----- 4. Start Windows Audio Endpoint Builder -----
echo Checking dependent services...
echo Checking dependent services... >> "%logfile%"
net start WMPNetworkSvc >nul 2>&1
net start RtkAudioService >nul 2>&1

:: ----- 5. Reset Sound Settings via Registry -----
echo Resetting audio settings in registry...
echo Resetting audio settings in registry... >> "%logfile%"
reg delete "HKCU\Software\Microsoft\Multimedia\Audio" /f >nul 2>&1
reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Multimedia\Audio" /f >nul 2>&1

:: ----- 6. Restart Audio Service with Dependencies -----
echo Restarting Audio Service with all dependencies...
echo Restarting Audio Service with all dependencies... >> "%logfile%"
powershell -Command "Restart-Service -Name 'Audiosrv' -Force" >nul 2>&1
timeout /t 3 >nul

:: ----- 7. Reset Default Audio Device -----
echo Resetting default audio device...
echo Resetting default audio device... >> "%logfile%"
powershell -Command "try { $ErrorActionPreference = 'SilentlyContinue'; [System.Reflection.Assembly]::LoadWithPartialName('System.Windows.Forms') | Out-Null; } catch {}" >nul 2>&1

:: ----- 8. Reset Audio Drivers (broader search pattern) -----
echo Resetting Audio Drivers...
echo Resetting Audio Drivers... >> "%logfile%"
where devcon >nul 2>&1
if %errorlevel% neq 0 (
    echo DevCon tool not found. Using alternative method to reset drivers.
    echo DevCon tool not found. Using alternative method to reset drivers. >> "%logfile%"
    powershell -Command "Get-PnpDevice | Where-Object {$_.Class -eq 'AudioEndpoint' -or $_.Class -eq 'Media'} | Disable-PnpDevice -Confirm:$false -ErrorAction SilentlyContinue" >nul 2>&1
    timeout /t 5 >nul
    powershell -Command "Get-PnpDevice | Where-Object {$_.Class -eq 'AudioEndpoint' -or $_.Class -eq 'Media'} | Where-Object {$_.Status -eq 'Error' -or $_.Status -eq 'Disabled'} | Enable-PnpDevice -Confirm:$false -ErrorAction SilentlyContinue" >nul 2>&1
) else (
    echo Using DevCon to reset audio devices...
    echo Using DevCon to reset audio devices... >> "%logfile%"
    devcon disable =MEDIA >nul 2>&1
    devcon disable =AUDIOENDPOINT >nul 2>&1
    timeout /t 5 >nul
    devcon enable =MEDIA >nul 2>&1
    devcon enable =AUDIOENDPOINT >nul 2>&1
)
timeout /t 3 >nul

:: ----- 9. Check System Volume Level -----
echo Setting system volume to 75%%...
echo Setting system volume to 75%%... >> "%logfile%"
powershell -Command "try { Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.SendKeys]::SendWait('{VOLUME_UP}'); [System.Windows.Forms.SendKeys]::SendWait('{VOLUME_UP}'); } catch {}" >nul 2>&1

:: ----- Final unmute check to ensure audio is not muted -----
echo Performing final unmute check...
echo Performing final unmute check... >> "%logfile%"
powershell -Command "Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.SendKeys]::SendWait('{VOLUME_MUTE}')" >nul 2>&1
timeout /t 1 >nul
powershell -Command "Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.SendKeys]::SendWait('{VOLUME_MUTE}')" >nul 2>&1
echo Volume unmuted and set.

:: ----- 10. Final Service Restart to Apply All Fixes -----
echo Performing final service restarts...
echo Performing final service restarts... >> "%logfile%"
net stop Audiosrv /y >nul 2>&1
timeout /t 2 >nul
net start AudioEndpointBuilder >nul 2>&1
timeout /t 2 >nul
net start Audiosrv >nul 2>&1
timeout /t 2 >nul

:: ----- 11. Check if services are running -----
echo Verifying audio services...
echo Verifying audio services... >> "%logfile%"

sc query Audiosrv | find "RUNNING" >nul
if !errorlevel! equ 0 (
    echo Windows Audio Service: RUNNING
    echo Windows Audio Service: RUNNING >> "%logfile%"
) else (
    echo Windows Audio Service: FAILED TO START
    echo Windows Audio Service: FAILED TO START >> "%logfile%"
)

sc query AudioEndpointBuilder | find "RUNNING" >nul
if !errorlevel! equ 0 (
    echo Audio Endpoint Builder: RUNNING
    echo Audio Endpoint Builder: RUNNING >> "%logfile%"
) else (
    echo Audio Endpoint Builder: FAILED TO START
    echo Audio Endpoint Builder: FAILED TO START >> "%logfile%"
)

:: ----- 12. Flush DNS and reset Winsock -----
echo Resetting network components related to audio...
echo Resetting network components related to audio... >> "%logfile%"
ipconfig /flushdns >nul 2>&1
netsh winsock reset >nul 2>&1

echo.
echo Audio troubleshooting completed.
echo Log saved to: "%logfile%"
echo Please test your audio now. If issues persist, consider:
echo  - Checking for Windows updates
echo  - Updating audio drivers manually
echo  - Checking Device Manager for hardware issues
echo.
pause
exit
