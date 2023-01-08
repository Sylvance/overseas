<?php

  //Configuration
  $access = "Set your Access Key here.";
  $userid = "Set your User Id here.";
  $passwd = "Set your Password here.";
  $wsdl = "Set Your WSDL Home here.";
  $operation = "ProcessChemicalReferenceData"; // Set the value of operation to either ProcessChemicalReferenceData or ProcessAcceptanceAuditPreCheck.
  $endpointurl = 'https://wwwcie.ups.com/webservices/DangerousGoodsUtility or  https://onlinetools.ups.com/webservices/DangerousGoodsUtility';
  $outputFileName = "XOLTResult.xml";

  function processChemicalReferenceData()
  {
      //create soap request
    $request['Request'] = array
    (
       'TransactionReference' => array
       (
            'CustomerContext' => 'PHP Test Client Program for Chemical Reference Data Request.',
       )
    );
    $request['ShipperNumber'] = 'Set your Shipper Number here.';
    $request['IDNumber'] = 'UN2561';
	$request['ProperShippingName'] = '3-METHYL-1-BUTENE';

    return $request;

  }

  function processAcceptanceAuditPreCheck()
  {
      //create soap request
    $request['Request'] = array
    (
       'TransactionReference' => array
       (
            'CustomerContext' => 'PHP Test Client Program for Acceptance Audit Pre-Check Request.',
       )
    );
	
	$shipment['ShipperNumber'] = 'Set your Shipper Number here.';
	
	$shipFromAddress['AddressLine'] = 'Clayallee 170';
	$shipFromAddress['City'] = 'Berlin';
	$shipFromAddress['PostalCode'] = '14191';
	$shipFromAddress['CountryCode'] = 'DE';
	$shipment['ShipFromAddress'] = $shipFromAddress;
	
	$shipToAddress['AddressLine'] = 'Gieener Str';
	$shipToAddress['City'] = 'Frankfurt';
	$shipToAddress['PostalCode'] = '60435';
	$shipToAddress['CountryCode'] = 'DE';
	$shipment['ShipToAddress'] = $shipToAddress;
   
	$service['Code'] = '011';
	$shipment['Service'] = $service;
	
	$shipment['RegulationSet'] = 'ADR';
	
	$package['PackageIdentifier'] = '1';
	$package['TransportationMode'] = 'GND';
	$package['EmergencyPhone'] = '1234567890';
	$package['EmergencyContact'] = 'Emergency Contact';
	
	$packageWeight['Weight'] = '1';
	
	$unitOfMeasurement['Code'] = 'KGS';
	$packageWeight['UnitOfMeasurement'] = $unitOfMeasurement;
	$package['PackageWeight'] = $packageWeight;
	
	$chemicalRecord['ChemicalRecordIdentifier'] = '1';
	$chemicalRecord['ReportableQuantity'] = 'RQ';
	$chemicalRecord['ClassDivisionNumber'] = '3';
	$chemicalRecord['IDNumber'] = 'UN2561';
	$chemicalRecord['PackagingGroupType'] = 'I';
	$chemicalRecord['Quantity'] = '1';
	$chemicalRecord['UOM'] = 'L';
	$chemicalRecord['PackagingInstructionCode'] = 'P001';
	$chemicalRecord['ProperShippingName'] = '3-METHYL-1-BUTENE';
	$chemicalRecord['PackagingType'] = 'Fiberboard Box';
	$chemicalRecord['PackagingTypeQuantity'] = '1';
	$chemicalRecord['CommodityRegulatedLevelCode'] = 'FR';
	$chemicalRecord['TransportCategory'] = '1';
	$chemicalRecord['TunnelRestrictionCode'] = 'D/E';
	$package['ChemicalRecord'] = $chemicalRecord;
	
	$shipment['Package'] = $package;
	
	$request['Shipment'] = $shipment;
    return $request;
  }

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
  	$client->__setLocation($endpointurl);


    //create soap header
    $usernameToken['Username'] = $userid;
    $usernameToken['Password'] = $passwd;
    $serviceAccessLicense['AccessLicenseNumber'] = $access;
    $upss['UsernameToken'] = $usernameToken;
    $upss['ServiceAccessToken'] = $serviceAccessLicense;

    $header = new SoapHeader('http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0','UPSSecurity',$upss);
    $client->__setSoapHeaders($header);

    if(strcmp($operation,"ProcessAcceptanceAuditPreCheck") == 0 )
    {
        //get response
  	    $resp = $client->__soapCall($operation,array(processAcceptanceAuditPreCheck()));

         //get status
        echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

        //save soap request and response to file
        $fw = fopen($outputFileName , 'w');
        fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
        fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
        fclose($fw);

    }
    else if(strcmp($operation,"ProcessChemicalReferenceData") == 0 )
    {
        $resp = $client->__soapCall($operation,array(processChemicalReferenceData()));

        //get status
        echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

  	    //save soap request and response to file
  	    $fw = fopen($outputFileName ,'w');
  	    fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
        fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
        fclose($fw);
    }
    else
    {
        echo "Invalid operation\n";
    }

  }
  catch(Exception $ex)
  {
  	print_r ( $ex );
  }

?>
