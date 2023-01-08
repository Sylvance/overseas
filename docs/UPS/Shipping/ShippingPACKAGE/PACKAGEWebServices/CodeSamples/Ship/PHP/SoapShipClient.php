<?php

  //Configuration
  $access = " Add License Key Here";
  $userid = " Add User Id Here";
  $passwd = " Add Password Here";
  $wsdl = " Add Wsdl File Here ";
  $operation = "ProcessShipment";
  $endpointurl = ' Add URL Here';
  $outputFileName = "XOLTResult.xml";

  function processShipment()
  {

      //create soap request
    $requestoption['RequestOption'] = 'nonvalidate';
    $request['Request'] = $requestoption;

    $shipment['Description'] = 'Ship WS test';
    $shipper['Name'] = 'ShipperName';
    $shipper['AttentionName'] = 'ShipperZs Attn Name';
    $shipper['TaxIdentificationNumber'] = '123456';
    $shipper['ShipperNumber'] = 'Your Shipper Number';
    $address['AddressLine'] = '2311 York Rd';
    $address['City'] = 'Timonium';
    $address['StateProvinceCode'] = 'MD';
    $address['PostalCode'] = '21093';
    $address['CountryCode'] = 'US';
    $shipper['Address'] = $address;
    $phone['Number'] = '1115554758';
    $phone['Extension'] = '1';
    $shipper['Phone'] = $phone;
    $shipment['Shipper'] = $shipper;

    $shipto['Name'] = 'Happy Dog Pet Supply';
    $shipto['AttentionName'] = 'Ship To Attention Name';
    $addressTo['AddressLine'] = 'GOERLITZER STR.1';
    $addressTo['City'] = 'Neuss';
    $addressTo['PostalCode'] = '41456';
    $addressTo['CountryCode'] = 'DE';
    $phone2['Number'] = '9225377171';
    $shipto['Address'] = $addressTo;
    $shipto['Phone'] = $phone2;
    $shipment['ShipTo'] = $shipto;

    $shipfrom['Name'] = 'T and T Designs';
    $shipfrom['AttentionName'] = '1160b_74';
    $addressFrom['AddressLine'] = '2311 York Rd';
    $addressFrom['City'] = 'Timonium';
    $addressFrom['StateProvinceCode'] = 'MD';
    $addressFrom['PostalCode'] = '21093';
    $addressFrom['CountryCode'] = 'US';
    $phone3['Number'] = '1234567890';
    $shipfrom['Address'] = $addressFrom;
    $shipfrom['Phone'] = $phone3;
    $shipment['ShipFrom'] = $shipfrom;

    $shipmentcharge['Type'] = '01';
    $creditcard['Type'] = '06';
    $creditcard['Number'] = '4716995287640625';
    $creditcard['SecurityCode'] = '864';
    $creditcard['ExpirationDate'] = '12/2013';
    $creditCardAddress['AddressLine'] = '2010 warsaw road';
    $creditCardAddress['City'] = 'Roswell';
    $creditCardAddress['StateProvinceCode'] = 'GA';
    $creditCardAddress['PostalCode'] = '30076';
    $creditCardAddress['CountryCode'] = 'US';
    $creditcard['Address'] = $creditCardAddress;
    $billshipper['CreditCard'] = $creditcard;
    $shipmentcharge['BillShipper'] = $billshipper;
    $paymentinformation['ShipmentCharge'] = $shipmentcharge;
    $shipment['PaymentInformation'] = $paymentinformation;

    $service['Code'] = '08';
    $service['Description'] = 'Expedited';
    $shipment['Service'] = $service;

    $internationalForm['FormType'] = '01';
    $internationalForm['InvoiceNumber'] = 'asdf123';
    $internationalForm['InvoiceDate'] = '20151225';
    $internationalForm['PurchaseOrderNumber'] = '999jjj777';
    $internationalForm['TermsOfShipment'] = 'CFR';
    $internationalForm['ReasonForExport'] = 'Sale';
    $internationalForm['Comments'] = 'Your Comments';
    $internationalForm['DeclarationStatement'] = 'Your Declaration Statement';
    $soldTo['Option'] = '01';
    $soldTo['AttentionName'] = 'Sold To Attn Name';
    $soldTo['Name'] = 'Sold To Name';
    $soldToPhone['Number'] = '1234567890';
    $soldToPhone['Extension'] = '1234';
    $soldTo['Phone'] = $soldToPhone;
    $soldToAddress['AddressLine'] = '34 Queen St';
    $soldToAddress['City'] = 'Frankfurt';
    $soldToAddress['PostalCode'] = '60547';
    $soldToAddress['CountryCode'] = 'DE';
    $soldTo['Address'] = $soldToAddress;
    $contact['SoldTo'] = $soldTo;
    $internationalForm['Contacts'] = $contact;
    $product['Description'] = 'Product 1';
    $product['CommodityCode'] = '111222AA';
    $product['OriginCountryCode'] = 'US';
    $unitProduct['Number'] = '147';
    $unitProduct['Value'] = '478';
    $uom['Code'] = 'BOX';
    $uom['Description'] = 'BOX';
    $unitProduct['UnitOfMeasurement'] = $uom;
    $product['Unit'] = $unitProduct;
    $productWeight['Weight'] = '10';
    $uomForWeight['Code'] = 'LBS';
    $uomForWeight['Description'] = 'LBS';
    $productWeight['UnitOfMeasurement'] = $uomForWeight;
    $product['ProductWeight'] = $productWeight;
    $internationalForm['Product'] = $product;
    $discount['MonetaryValue'] = '100';
    $internationalForm['Discount'] = $discount;
    $freight['MonetaryValue'] = '50';
    $internationalForm['FreightCharges'] = $freight;
    $insurance['MonetaryValue'] = '200';
    $internationalForm['InsuranceCharges'] = $insurance;
    $otherCharges['MonetaryValue'] = '50';
    $otherCharges['Description'] = 'Misc';
    $internationalForm['OtherCharges'] = $otherCharges;
    $internationalForm['CurrencyCode'] = 'USD';
    $shpServiceOptions['InternationalForms'] = $internationalForm;
    $shipment['ShipmentServiceOptions'] = $shpServiceOptions;


    $package['Description'] = '';
    $packaging['Code'] = '02';
    $packaging['Description'] = 'Nails';
    $package['Packaging'] = $packaging;
    $unit['Code'] = 'IN';
    $unit['Description'] = 'Inches';
    $dimensions['UnitOfMeasurement'] = $unit;
    $dimensions['Length'] = '7';
    $dimensions['Width'] = '5';
    $dimensions['Height'] = '2';
    $package['Dimensions'] = $dimensions;
    $unit2['Code'] = 'LBS';
    $unit2['Description'] = 'Pounds';
    $packageweight['UnitOfMeasurement'] = $unit2;
    $packageweight['Weight'] = '10';
    $package['PackageWeight'] = $packageweight;
    $shipment['Package'] = $package;

    $labelimageformat['Code'] = 'GIF';
    $labelimageformat['Description'] = 'GIF';
    $labelspecification['LabelImageFormat'] = $labelimageformat;
    $labelspecification['HTTPUserAgent'] = 'Mozilla/4.5';
    $shipment['LabelSpecification'] = $labelspecification;
    $request['Shipment'] = $shipment;

    echo "Request.......\n";
	print_r($request);
    echo "\n\n";
    return $request;

  }

  function processShipConfirm()
  {

    //create soap request

  }

  function processShipAccept()
  {
    //create soap request
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

    if(strcmp($operation,"ProcessShipment") == 0 )
    {
        //get response
  	$resp = $client->__soapCall('ProcessShipment',array(processShipment()));

         //get status
        echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

        //save soap request and response to file
        $fw = fopen($outputFileName , 'w');
        fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
        fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
        fclose($fw);

    }
    else if (strcmp($operation , "ProcessShipConfirm") == 0)
    {
            //get response
  	$resp = $client->__soapCall('ProcessShipConfirm',array(processShipConfirm()));

         //get status
        echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

        //save soap request and response to file
        $fw = fopen($outputFileName , 'w');
        fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
        fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
        fclose($fw);

    }
    else
    {
        $resp = $client->__soapCall('ProcessShipeAccept',array(processShipAccept()));

        //get status
        echo "Response Status: " . $resp->Response->ResponseStatus->Description ."\n";

  	//save soap request and response to file
  	$fw = fopen($outputFileName ,'w');
  	fwrite($fw , "Request: \n" . $client->__getLastRequest() . "\n");
        fwrite($fw , "Response: \n" . $client->__getLastResponse() . "\n");
        fclose($fw);
    }

  }
  catch(Exception $ex)
  {
  	print_r ($ex);
  }

?>
