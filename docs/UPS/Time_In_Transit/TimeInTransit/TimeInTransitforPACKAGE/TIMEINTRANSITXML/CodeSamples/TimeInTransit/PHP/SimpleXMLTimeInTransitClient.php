<?php
try {
	
	// configuration
	$access = " Add License Key Here";
	$userid = " Add User Id Here";
	$passwd = " Add Password Here";
	
	$endpointurl = "  Add URL Here";
	$outputFileName = "XOLTResult.xml";
	
	// create a simple xml object for AccessRequest & TNTRequest
	$accessRequesttXML = new SimpleXMLElement ( "<AccessRequest></AccessRequest>" );
	$tntRequestXML = new SimpleXMLElement ( "<TimeInTransitRequest></TimeInTransitRequest>" );
	
	// create AccessRequest XML
	$accessRequesttXML->addChild ( "AccessLicenseNumber", $access );
	$accessRequesttXML->addChild ( "UserId", $userid );
	$accessRequesttXML->addChild ( "Password", $passwd );
	
	// create TNTRequest XML
	$request = $tntRequestXML->addChild ( 'Request' );
	$request->addChild ( "RequestAction", "TimeInTransit" );
	
	$transitFrom = $tntRequestXML->addChild ( 'TransitFrom' );
	$addressArtifactFormatTF = $transitFrom->addChild ( 'AddressArtifactFormat' );
	$addressArtifactFormatTF->addChild ( "PoliticalDivision2", "Carrickmacross" );
	$addressArtifactFormatTF->addChild ( "PostcodePrimaryLow", "" );
	$addressArtifactFormatTF->addChild ( "CountryCode", "IE" );
	
	$transitTo = $tntRequestXML->addChild ( 'TransitTo' );
	$addressArtifactFormatTT = $transitTo->addChild ( 'AddressArtifactFormat' );
	$addressArtifactFormatTT->addChild ( "PoliticalDivision2", "Stockholm" );
	$addressArtifactFormatTT->addChild ( "PostcodePrimaryLow", "11187" );
	$addressArtifactFormatTT->addChild ( "CountryCode", "SE" );
	
	$tntRequestXML->addChild ( "PickupDate", "20161120" );
	
	$tntRequestXML->addChild ( "MaximumListSize", "10" );
	
	$invoiceLineTotal = $tntRequestXML->addChild ( "InvoiceLineTotal" );
	$invoiceLineTotal->addChild ( "CurrencyCode", "EUR" );
	$invoiceLineTotal->addChild ( "MonetaryValue", "10" );
	
	$shipmentWeight = $tntRequestXML->addChild ( "ShipmentWeight" );
	$unitOfMeasurement = $shipmentWeight->addChild ( "UnitOfMeasurement" );
	$unitOfMeasurement->addChild ( "Code", "KGS" );
	$unitOfMeasurement->addChild ( "Description", "Kilograms" );
	$shipmentWeight->addChild ( "Weight", "10" );
	
	$tntRequestXML->addChild ( "TotalPackagesInShipment", "1" );
	
	$requestXML = $accessRequesttXML->asXML () . $tntRequestXML->asXML ();
	
	// create Post request
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
} catch ( Exception $ex ) {
	echo $ex;
}
?>