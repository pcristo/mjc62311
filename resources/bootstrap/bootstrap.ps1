
# Requires powershell v3
$CONFIG = (Get-Content ../../src/config.json) -join "`n" | ConvertFrom-Json

# TODO move this to a config to be read by java
$logServerIP = $CONFIG.logServerIP
$logServerPort = $CONFIG.logServerPort
$logServerPath = $CONFIG.logServerPath
$logServerHome = $CONFIG.logServerHome
$logServerUsername = $CONFIG.logServerUsername
$logServerPassword = $CONFIG.logServerPassword

$jarDir = $CONFIG.jars
$outDIr = $CONFIG.out

if ($logServerIP -match "localhost") {
	echo "Starting local logging server"
	
	$loggerProcess = Start-Process -NoNewWindow -PassThru "java" "-classpath $logServerHome\\$jarDir\\*;$logServerHome\\$outDir\\; logger.LoggerServer"
	
	
	$stop = Read-Host "X to stop servers"
	if($stop -match "X") {
		Stop-Process $loggerProcess.id
		echo "Logging server stopped";
	}
	
		
} else {
	
	$loggerProcess = Start-Process -NoNewWindow -PassThru ".\plink.exe" "$logServerUsername@$logServerIP -pw $logServerPassword java -cp $logServerHome/$jarDir/*:$logServerHome/$outDir/: logger.LoggerServer"

	$stop = Read-Host "X to stop servers"
	if($stop -match "X") {
		.\plink.exe $logServerUsername@$logServerIP -pw $logServerPassword "lsof -i:$logServerPort -t | xargs kill"
		Stop-Process $loggerProcess.id
		echo "Logging server stopped";
	}

}




