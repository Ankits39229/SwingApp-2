@echo off
title Network Troubleshooting

:: Show initial message
echo -------------------------------------------
echo Network Troubleshooting in Progress...
echo -------------------------------------------
echo.

:: Step 1: Check if the computer can reach the default gateway
echo Pinging default gateway...
ping 192.168.1.1 -n 4
echo.

:: Step 2: Check if the computer can reach Google's DNS (8.8.8.8)
echo Pinging Google's DNS (8.8.8.8)...
ping 8.8.8.8 -n 4
echo.

:: Step 3: Release and renew IP configuration
echo Releasing IP address...
ipconfig /release
echo Renewing IP address...
ipconfig /renew
echo.

:: Step 4: Flush DNS Cache
echo Flushing DNS cache...
ipconfig /flushdns
echo.

:: Step 5: Display the current IP configuration
echo Displaying IP configuration...
ipconfig /all
echo.

:: Step 6: Check network interfaces status
echo Checking network interfaces status...
netstat -e
echo.

:: Step 7: Reset TCP/IP stack
echo Resetting TCP/IP stack...
netsh int ip reset
echo.

:: Step 8: Restart network adapter (optional but can help resolve issues)
echo Restarting network adapter...
netsh interface set interface "Ethernet" admin=disable
netsh interface set interface "Ethernet" admin=enable
echo.

:: Final message
echo -------------------------------------------
echo Network Troubleshooting Complete.
@REM echo Please check the results above.

timeout /t 2 /nobreak >nul
