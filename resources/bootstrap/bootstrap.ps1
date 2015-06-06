
# Requires powershell v3
$CONFIG = (Get-Content ../../src/config.json) -join "`n" | ConvertFrom-Json

# TODO move this to a config to be read by java
$logServerIP = $CONFIG.logServerIP
$logServerPort = $CONFIG.logServerPort
$projectHome = $CONFIG.projectHome
$logServerHome = $CONFIG.logServerHome
$logServerUsername = $CONFIG.logServerUsername
$logServerPassword = $CONFIG.logServerPassword

$backup_logServerIP = $CONFIG.backup_logServerIP

$jarDir = $CONFIG.jars
$outDIr = $CONFIG.out

$rmiPath = $CONFIG.rmiPath
$businessServerIP = $CONFIG.businessServerIP

$brokerServerIP = $CONFIG.brokerServerIP

if ($logServerIP -match "localhost") {
	$loggerProcess = Start-Process -NoNewWindow -PassThru "java" "-classpath $projectHome\\$jarDir\\*;$projectHome\\$outDir\\; logger.LoggerServer"
	echo "Local Logger Server started!"
	
} else {
	#$loggerProcess = Start-Process -NoNewWindow -PassThru ".\plink.exe" "$logServerUsername@$logServerIP -pw $logServerPassword java -cp $projectHome/$jarDir/*:$projectHome/$outDir/: logger.LoggerServer"
	#echo "Remote Logger Server started!"
}

if ($backup_logServerIP -match "localhost") {
	$backup_loggerProcess = Start-Process -NoNewWindow -PassThru "java" "-classpath $projectHome\\$jarDir\\*;$projectHome\\$outDir\\; logger.LoggerServer"
	echo "Local BACKUP Logger Server started!"
	
} 


Start-Sleep -s 2

if($businessServerIP -match "localhost") {
	# Start RMI
#	$tmp =  $projectHome + $outDir
#	cd $tmp
#	$rmiProcess = Start-Process -NoNewWindow -PassThru $rmiPath
#	echo $rmiProcess	

	# Start businessServer
	$businessProcess = Start-Process -NoNewWindow -PassThru "java"  "-classpath $projectHome$jarDir\\*;$projectHome$outDir\; business.Business"
	echo "Local Business Server started!"
		
#	$tmp = $projectHome + "\\resources\\bootstrap"
#	cd $tmp

}



Start-Sleep -s 2

if($brokerServerIP -match "localhost") {
	
	$brokerProcess = Start-Process -NoNewWindow -PassThru "java"  "-classpath $projectHome$jarDir\\*;$projectHome$outDir\; stockexchange.broker.Broker"
	echo "Local Broker Server Started!"
}


Start-Sleep -s 2

$stop = Read-Host "X to stop server"
if($stop -match "X") {
	if($logServerIP -match "localhost") {
		Stop-Process $loggerProcess.id
		echo "Logging server stopped";
	} else {
	#	.\plink.exe $logServerUsername@$logServerIP -pw $logServerPassword "lsof -i:$logServerPort -t | xargs kill"
	#	Stop-Process $loggerProcess.id
	#	echo "Logging server stopped";
	}
	
	
	if($backup_logServerIP -match "localhost") {
		Stop-Process $backup_loggerProcess.id
		echo "Local BACKUP Logging server stopped";
	} 
	
	
	if($businessServerIP -match "localhost") {
#		Stop-Process $rmiProcess
		kill $businessProcess
		echo "Business server stopped"
	}
	
	if($brokerServerIP -match "localhost") {
#		Stop-Process $rmiProcess
		kill $brokerProcess
		echo "Broker server stopped"
	}
}




