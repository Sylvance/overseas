<?php

// Configuration
$accessLicenseNumber = "Add License Key Here";
$userId = "Add User Id Here";
$password = "Add Password Here";

$endpointurl = 'Add URL Here';
$outputFileName = "XOLTResult.xml";

try {
	
	// Create AccessRequest XMl
	$accessRequestXML = new SimpleXMLElement ( "<AccessRequest></AccessRequest>" );
	$accessRequestXML->addChild ( "AccessLicenseNumber", $accessLicenseNumber );
	$accessRequestXML->addChild ( "UserId", $userId );
	$accessRequestXML->addChild ( "Password", $password );
	
	// Create ShipmentAcceptRequest XMl
	$shipmentAcceptRequestXML = new SimpleXMLElement ( "<ShipmentAcceptRequest ></ShipmentAcceptRequest >" );
	$request = $shipmentAcceptRequestXML->addChild ( 'Request' );
	$request->addChild ( "RequestAction", "01" );
	
	$shipmentAcceptRequestXML->addChild ( "ShipmentDigest", "Your ShipmentDigest" );
	
	$requestXML = $accessRequestXML->asXML () . $shipmentAcceptRequestXML->asXML ();
	
	$ch = curl_init();
	curl_setopt( $ch, CURLOPT_URL, $endpointurl );
	curl_setopt( $ch, CURLOPT_POST, true );
	curl_setopt( $ch, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));
	curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
	curl_setopt( $ch, CURLOPT_POSTFIELDS, $requestXML );
	$response = curl_exec($ch);
	curl_close($ch);
	
	if ($response == false) {
		throw new Exception ( "Bad data." );
	} else {
		// save request and response to file
		$fw = fopen ( $outputFileName, 'w' );
		fwrite ( $fw, "Request: \n" . $requestXML . "\n" );
		fwrite ( $fw, "Response: \n" . $response . "\n" );
		fclose ( $fw );
		
		// get response status
		$resp = new SimpleXMLElement ( $response );
		echo $resp->Response->ResponseStatusDescription . "\n";
	}
	
	Header ( 'Content-type: text/xml' );
} catch ( Exception $ex ) {
	echo $ex;
}

?>

