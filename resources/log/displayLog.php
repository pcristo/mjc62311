<?php
$string = file_get_contents("C:\\Users\\b0467851\\WORK\\school\\DistributedSystems\\project\\src\\config.json");

$wipe = $_GET['wipe'];

$json = json_decode($string, true);
$logFileLocation = $json['projectHome'] . $json['logServerFile'];

if($wipe == "true") {
	file_put_contents($logFileLocation, "Log Server wiped at " . date("Y/m/d") ."<br/>");
}

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
