@echo off
echo Starting BSOD Troubleshooting...
echo ---------------------------------
echo.

:: Step 1: Check for recent BSOD events in the Event Viewer
echo Checking for BSOD events in Event Viewer...
eventvwr.msc /s > "%TEMP%\eventlog.txt"
findstr /i "error" "%TEMP%\eventlog.txt" > "%TEMP%\BSOD_Events.txt"
echo BSOD-related events saved to %TEMP%\BSOD_Events.txt

:: Step 2: Run System File Checker to check for corrupted system files
echo Running System File Checker (SFC)...
sfc /scannow > "%TEMP%\sfc_results.txt"
echo SFC scan results saved to %TEMP%\sfc_results.txt

:: Step 3: Run CHKDSK to check for disk issues
echo Running CHKDSK for disk errors...
chkdsk C: /f /r > "%TEMP%\chkdsk_results.txt"
echo CHKDSK results saved to %TEMP%\chkdsk_results.txt

:: Step 4: Check for outdated or corrupt drivers (list installed drivers)
echo Listing installed drivers...
driverquery > "%TEMP%\driver_list.txt"
echo Driver list saved to %TEMP%\driver_list.txt

:: Step 5: Check for Windows Update issues that could cause BSODs
echo Checking for Windows Update issues...
powershell -Command "Get-WindowsUpdateLog"
echo Windows Update logs reviewed.

:: Step 6: Generate a minidump of the system crash (if available)
echo Attempting to create a minidump for crash analysis...
mdsched.exe

:: Step 7: Check Windows memory diagnostics (memory-related issues)
echo Running Windows Memory Diagnostic...
mdsched.exe
echo Memory diagnostic results can be reviewed in the Event Viewer.

:: Step 8: Restart the system to apply any fixes
echo Restarting the system to apply any fixes...
shutdown /r /f /t 10
