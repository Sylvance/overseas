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
	
	// Create ShipmentConfirmRequest XMl
	$shipmentConfirmRequestXML = new SimpleXMLElement ( "<ShipmentConfirmRequest ></ShipmentConfirmRequest>" );
	$request = $shipmentConfirmRequestXML->addChild ( 'Request' );
	$request->addChild ( "RequestAction", "ShipConfirm" );
	$request->addChild ( "RequestOption", "nonvalidate" );
	
	$labelSpecification = $shipmentConfirmRequestXML->addChild ( 'LabelSpecification' );
	$labelSpecification->addChild ( "HTTPUserAgent", "" );
	$labelPrintMethod = $labelSpecification->addChild ( 'LabelPrintMethod' );
	$labelPrintMethod->addChild ( "Code", "GIF" );
	$labelPrintMethod->addChild ( "Description", "" );
	$labelImageFormat = $labelSpecification->addChild ( 'LabelImageFormat' );
	$labelImageFormat->addChild ( "Code", "GIF" );
	$labelImageFormat->addChild ( "Description", "" );
	
	$shipment = $shipmentConfirmRequestXML->addChild ( 'Shipment' );
	$shipment->addChild ( "Description", "" );
	$rateInformation = $shipment->addChild ( 'RateInformation' );
	$rateInformation->addChild ( "NegotiatedRatesIndicator", "" );
	
	$shipper = $shipment->addChild ( 'Shipper' );
	$shipper->addChild ( "Name", "Shipper Name" );
	$shipper->addChild ( "PhoneNumber", "1234567890" );
	$shipper->addChild ( "TaxIdentificationNumber", "1234567877" );
	$shipper->addChild ( "ShipperNumber", "Your Shipper Number" );
	$shipperAddress = $shipper->addChild ( 'Address' );
	$shipperAddress->addChild ( "AddressLine1", "Address line 1" );
	$shipperAddress->addChild ( "City", "Timonium" );
	$shipperAddress->addChild ( "StateProvinceCode", "MD" );
	$shipperAddress->addChild ( "PostalCode", "Postal code" );
	$shipperAddress->addChild ( "CountryCode", "US" );
	
	$shipTo = $shipment->addChild ( 'ShipTo' );
	$shipTo->addChild ( "CompanyName", "Company name" );
	$shipTo->addChild ( "AttentionName", "Ship to attention name" );
	$shipTo->addChild ( "PhoneNumber", "Phone number" );
	$shipToAddress = $shipTo->addChild ( 'Address' );
	$shipToAddress->addChild ( "AddressLine1", "Address line 1" );
	$shipToAddress->addChild ( "City", "Vero Beach" );
	$shipToAddress->addChild ( "StateProvinceCode", "FL" );
	$shipToAddress->addChild ( "PostalCode", "Postal code" );
	$shipToAddress->addChild ( "CountryCode", "US" );
	
	$shipFrom = $shipment->addChild ( 'ShipFrom' );
	$shipFrom->addChild ( "CompanyName", "Company name" );
	$shipFrom->addChild ( "AttentionName", "Ship From Attention name" );
	$shipFrom->addChild ( "PhoneNumber", "1234567890" );
	$shipFrom->addChild ( "TaxIdentificationNumber", "1234567877" );
	$shipFromAddress = $shipFrom->addChild ( 'Address' );
	$shipFromAddress->addChild ( "AddressLine1", "Address line 1" );
	$shipFromAddress->addChild ( "City", "Vero Beach" );
	$shipFromAddress->addChild ( "StateProvinceCode", "FL" );
	$shipFromAddress->addChild ( "PostalCode", "Postal code" );
	$shipFromAddress->addChild ( "CountryCode", "US" );
	
	$paymentInformation = $shipment->addChild ( 'PaymentInformation' );
	$prepaid = $paymentInformation->addChild ( 'Prepaid' );
	$billShipper = $prepaid->addChild ( 'BillShipper' );
	$billShipper->addChild ( "AccountNumber", "Your Account Number" );
	
	$service = $shipment->addChild ( 'Service' );
	$service->addChild ( "Code", "02" );
	$service->addChild ( "Description", "" );
	
	$package = $shipment->addChild ( 'Package' );
	$package->addChild ( "Description", "" );
	$packagingType = $package->addChild ( 'PackagingType' );
	$packagingType->addChild ( "Code", "02" );
	$packagingType->addChild ( "Description", "" );
	$packageWeight = $package->addChild ( 'PackageWeight' );
	$packageWeight->addChild ( "Weight", "60" );
	$packageWeight->addChild ( 'UnitOfMeasurement' );
	
	$requestXML = $accessRequestXML->asXML () . $shipmentConfirmRequestXML->asXML ();
	
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

