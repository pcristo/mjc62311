
# Requires powershell v3
$CONFIG = (Get-Content config.json) -join "`n" | ConvertFrom-Json

# TODO move this to a config to be read by java
$logServerIP = $CONFIG.logServerIP
$logServerPort = $CONFIG.logServerPort
$logServerPath = $CONFIG.logServerPath
$logServerUsername = $CONFIG.logServerUsername
$logServerPassword = $CONFIG.logServerPassword



if ($logServerIP -match "localhost") {
	echo "Starting localhost"
	cd $logServerPath
	$loggerProcess = Start-Process -NoNewWindow -PassThru java logger/LoggerServer
	
	$stop = Read-Host "X to stop servers"
	if($stop -match "X") {
		Stop-Process $loggerProcess.id
	}
		
} else {
	echo "More work to do"
}




cd ..\resources\bootstrap
