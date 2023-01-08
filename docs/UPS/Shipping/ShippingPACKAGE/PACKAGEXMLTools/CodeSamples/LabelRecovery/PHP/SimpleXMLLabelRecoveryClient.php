<?php

// Configuration
$accessLicenseNumber = "Add License Key Here";
$userId = "Add User Id Here";
$password = "Add Password Here";

$endpointurl = 'Add URL Here';
$outputFileName = "XOLTResult.xml";

try {
	
	// Create AccessRequest XMl
	$accessRequesttXML = new SimpleXMLElement ( "<AccessRequest></AccessRequest>" );
	$accessRequesttXML->addChild ( "AccessLicenseNumber", $accessLicenseNumber );
	$accessRequesttXML->addChild ( "UserId", $userId );
	$accessRequesttXML->addChild ( "Password", $password );
	
	// Create LabelRecoveryRequest XMl
	$labelRecoveryRequestXML = new SimpleXMLElement ( "<LabelRecoveryRequest ></LabelRecoveryRequest >" );
	$request = $labelRecoveryRequestXML->addChild ( 'Request' );
	$request->addChild ( "RequestAction", "LabelRecovery" );
	
	$labelSpecification = $labelRecoveryRequestXML->addChild ( 'LabelSpecification' );
	$labelSpecification->addChild ( "HTTPUserAgent" );
	$labelImageFormat = $labelSpecification->addChild ( 'LabelImageFormat' );
	$labelImageFormat->addChild ( "Code", "GIF" );
	
	$labelDelivery = $labelRecoveryRequestXML->addChild ( 'LabelDelivery' );
	$labelDelivery->addChild ( "LabelLinkIndicator" );
	$labelDelivery->addChild ( "ResendEMailIndicator" );
	
	$labelRecoveryRequestXML->addChild ( "TrackingNumber", "Your Tracking Number" );
	
	$requestXML = $accessRequesttXML->asXML () . $labelRecoveryRequestXML->asXML ();
	
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