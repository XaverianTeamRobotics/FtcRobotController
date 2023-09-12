$routerIp = Read-Host -Prompt "Enter the IP to connect to"

$routerIp = $routerIp + ":55555"
adb connect $routerIp