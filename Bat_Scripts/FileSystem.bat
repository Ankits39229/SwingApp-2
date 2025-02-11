@echo off
:: Script to troubleshoot and repair file system issues on Windows

echo Starting file system check and repair...

:: Run Check Disk (chkdsk) to check and repair file system errors
echo Running Check Disk (chkdsk)...
chkdsk C: /f /r /x
echo Check Disk completed. If there were any issues, it will have been repaired.

:: Pause to allow user to read any messages from chkdsk
@REM pause

:: Run System File Checker (sfc) to scan and repair system files
echo Running System File Checker (sfc)...
sfc /scannow
echo System File Checker completed. If any corrupted files were found, they have been repaired.

:: Pause to allow user to read any messages from sfc
@REM pause

:: Optionally, you can add DISM to repair the system image if needed
echo Running Deployment Imaging Service and Management Tool (DISM)...
DISM /Online /Cleanup-Image /RestoreHealth
echo DISM completed. If there were any issues with the system image, they have been repaired.

:: Final pause to allow user to read any final messages

timeout /t 2 /nobreak >nul
