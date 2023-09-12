Read-Host -Prompt "Plug in the robot via USB then press ENTER...."

adb tcpip 55555

$routerIp = Read-Host -Prompt "Unplug the USB cable and enter the IP to connect to"

$routerIp = $routerIp + ":55555"
adb connect $routerIp