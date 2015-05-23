<?php
$string = file_get_contents("/home/java/project/src/config.json");

$json = json_decode($string, true);
$logFileLocation = $json['logServerFile'];


//$log = file_get_contents($logFileLocation);

//echo $log;

$handle = fopen($logFileLocation, "r");

if($handle) {
	while(($line = fgets($handle)) !== false) {
		echo $line . "<br/>";
	}
	fclose($handle);
}




?>
