
# Requires powershell v3
$CONFIG = (Get-Content ../../src/config.json) -join "`n" | ConvertFrom-Json

# TODO move this to a config to be read by java
$logServerIP = $CONFIG.logServerIP
$logServerPort = $CONFIG.logServerPort
$logServerPath = $CONFIG.logServerPath
$logServerUsername = $CONFIG.logServerUsername
$logServerPassword = $CONFIG.logServerPassword

$jsonJar = ($PSScriptRoot + $CONFIG.jsonJar)


if ($logServerIP -match "localhost") {
	echo "Starting logging server"
	cd $logServerPath 
	$loggerProcess = Start-Process -NoNewWindow -PassThru "java" "-classpath $jsonJar logger/LoggerServer"
	
	
	$stop = Read-Host "X to stop servers"
	if($stop -match "X") {
		Stop-Process $loggerProcess.id
		echo "Logging server stopped";
	}
		
} else {
	echo "More work to do"
}




cd ..\resources\bootstrap
