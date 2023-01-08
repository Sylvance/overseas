<?php

// Configuration
$accessLicenseNumber = "Add License Key Here";
$userId = "Add User Id Here";
$password = "Add Password Here";

$endpointurl = 'Add URL Here';
$outputFileName = "XOLTResult.xml";

try {
	
	$accessRequestXML = new SimpleXMLElement ( "<AccessRequest></AccessRequest>" );
	$locatorRequestXML = new SimpleXMLElement ( "<LocatorRequest ></LocatorRequest >" );
	
	$accessRequestXML->addChild ( "AccessLicenseNumber", $accessLicenseNumber );
	$accessRequestXML->addChild ( "UserId", $userId );
	$accessRequestXML->addChild ( "Password", $password );
	
	$request = $locatorRequestXML->addChild ( 'Request' );
	$request->addChild ( "RequestAction", "Locator" );
	$request->addChild ( "RequestOption", "3" );
	
	$originAddress = $locatorRequestXML->addChild ( 'OriginAddress' );
	$addressKeyFormat = $originAddress->addChild ( 'AddressKeyFormat' );
	$addressKeyFormat->addChild ( "AddressLine", "Your Address Line" );
	$addressKeyFormat->addChild ( "PoliticalDivision2", "Atlanta" );
	$addressKeyFormat->addChild ( "PoliticalDivision1", "GA" );
	$addressKeyFormat->addChild ( "PostcodePrimaryLow", "85281" );
	$addressKeyFormat->addChild ( "CountryCode", "US" );
	
	$translate = $locatorRequestXML->addChild ( 'Translate' );
	$translate->addChild ( "LanguageCode", "ENG" );
	
	$unitOfMeasurement = $locatorRequestXML->addChild ( 'UnitOfMeasurement' );
	$unitOfMeasurement->addChild ( "Code", "MI" );
	
	$locatorRequestXML->addChild ( "LocationID", "49249" );
	
	$requestXML = $accessRequestXML->asXML () . $locatorRequestXML->asXML ();
	
	$form = array (
			'http' => array (
					'method' => 'POST',
					'header' => 'Content-type: application/x-www-form-urlencoded',
					'content' => "$requestXML" 
			) 
	);
	
	$request = stream_context_create ( $form );
	$browser = fopen ( $endpointurl, 'rb', false, $request );
	if (! $browser) {
		throw new Exception ( "Connection failed." );
	}
	
	// get response
	$response = stream_get_contents ( $browser );
	fclose ( $browser );
	
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

