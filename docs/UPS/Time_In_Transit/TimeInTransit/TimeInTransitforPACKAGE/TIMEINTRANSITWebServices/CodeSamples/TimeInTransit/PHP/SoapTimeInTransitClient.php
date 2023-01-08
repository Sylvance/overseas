<?php

  //Configuration
  $access = " Add License Key Here";
  $userid = " Add User Id Here";
  $passwd = " Add Password Here";
  $wsdl = " Add Wsdl File Here ";
  $operation = "ProcessTimeInTransit";
  $endpointurl = ' Add URL Here';
  $outputFileName = "XOLTResult.xml";

  try
  {

    $mode = array
    (
         'soap_version' => 'SOAP_1_1',  // use soap 1.1 client
         'trace' => 1
    );

    // initialize soap client
  	$client = new SoapClient($wsdl , $mode);

  	//set endpoint url
  	$client->__setLocation( $endpointurl );

    //create soap header
    $usernameToken['Username'] = $userid;
    $usernameToken['Password'] = $passwd;
    $serviceAccessLicense['AccessLicenseNumber'] = $access;
    $upss['UsernameToken'] = $usernameToken;
    $upss['ServiceAccessToken'] = $serviceAccessLicense;

    $header = new SoapHeader('http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0','UPSSecurity',$upss);
    $client->__setSoapHeaders($header);

    //create soap request
    $requestoption['RequestOption'] = 'TNT';
    $request['Request'] = $requestoption;

    $addressFrom['City'] = 'Roswell';
    $addressFrom['CountryCode'] = 'US';
    $addressFrom['PostalCode'] = '30076';
    $addressFrom['StateProvinceCode'] = 'GA';
    $shipFrom['Address'] = $addressFrom;
    $request['ShipFrom'] = $shipFrom;

    $addressTo['City'] = 'Roswell';
	$addressTo['CountryCode'] = 'US';
	$addressTo['PostalCode'] = '30076';
    $addressTo['StateProvinceCode'] = 'GA';
    $shipTo['Address'] = $addressTo;
    $request['ShipTo'] = $shipTo;

    $pickup['Date'] = '20110730';
    $request['Pickup'] = $pickup;

    $unitOfMeasurement['Code'] = 'KGS';
    $unitOfMeasurement['Description'] ='Kilograms';
    $shipmentWeight['UnitOfMeasurement'] = $unitOfMeasurement;
    $shipmentWeight['Weight'] = '10';
    $request['ShipmentWeight'] = $shipmentWeight;

    $request['TotalPackagesInShipment'] = '1';

    $invoiceLineTotal['CurrencyCode'] = 'CAD';
    $invoiceLineTotal['MonetaryValue'] = '10';
    $request['InvoiceLineTotal'] = $invoiceLineTotal;

    $request['MaximumListSize'] = '1';

    echo "Request.......\n";
	print_r($request);
    echo "\n\n";

    //get response
  	$resp = $client->__soapCall($operation ,array($request));

  	 //get status
    echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

  	//save soap request and response to file
  	$fw = fopen($outputFileName ,'w');
  	fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
    fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
    fclose($fw);

  }
  catch(Exception $e)
  {
    print_r($e);
  }

?>