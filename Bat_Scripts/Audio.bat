@echo off
:: Audio Troubleshooting Script for Windows (No Admin Privileges)
echo Troubleshooting Audio Issues on Windows...
echo Please wait while we attempt to diagnose and resolve audio issues.
echo.

:: 1. Check audio device settings (open Volume Mixer)
echo 1. Checking audio device settings...
sndvol
echo.

:: 2. Restart audio services (Skip restart as admin privileges are needed)
echo 2. Audio services could not be restarted without administrative privileges.
echo You can manually restart the audio services from the Services app.
echo.

:: 3. Check for driver updates (opens Device Manager)
echo 3. Checking for driver updates...
devmgmt.msc
echo.

:: 4. Disable and re-enable audio device (opens Device Manager)
echo 4. Disabling and re-enabling the audio device...
devmgmt.msc
echo Please right-click on your audio device, choose "Disable," and then "Enable."
echo.

:: 5. Run Windows audio troubleshooter
echo 5. Running Windows audio troubleshooter...
msdt.exe -id Audiotroubleshooter
echo.

:: 6. Reset audio settings to default
echo 6. Resetting audio settings to default...
control.exe mmsys.cpl,,0
echo Default audio settings applied.
echo.

:: 7. Check for Windows updates (User may need to check manually)
echo 7. Checking for pending Windows updates...
wuauclt.exe /detectnow
echo Windows updates checked.
echo.

:: 8. Check for disabled audio devices (without admin rights)
echo 8. Checking for disabled audio devices...
powershell -Command "Get-PnpDevice -Class Sound | Where-Object { $_.Status -eq 'Disabled' }"
if %errorlevel% equ 0 (
    echo All audio devices are enabled.
) else (
    echo Some audio devices are disabled. Please enable them manually in Device Manager.
)
echo.

:: 9. Final status check for audio driver (without admin rights)
echo 9. Checking audio driver status...
powershell -Command "Get-PnpDevice -Class Sound | ForEach-Object { $_.Status }"
if %errorlevel% equ 0 (
    echo Audio drivers appear to be functioning correctly.
) else (
    echo Issues detected with audio drivers. Please check for updates or reinstall them manually.
)
echo.

:: Troubleshooting complete
echo Troubleshooting complete. Please check if your audio issue is resolved.
pause
exit
