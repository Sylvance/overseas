 no warnings; # turn off warnings
 
 use XML::Compile::WSDL11;
 use XML::Compile::SOAP11;
 use XML::Compile::Transport::SOAPHTTP;
 use HTTP::Request;
 use HTTP::Response;
 use Data::Dumper;
 
 #Configuration
 $access = " Add License Key Here";
 $userid = " Add User Id Here";
 $passwd = " Add Password Here";
 $operation = "ProcessShipment";
 $endpointurl = " Add URL Here";
 $wsdlfile = " Add Wsdl File Here ";
 $schemadir = "Add Schema Location Here";
 $outputFileName = "XOLTResult.xml";
 
 sub processShipment
 {
 	my $request =
 	{
 		UPSSecurity =>  
	  	{
		   UsernameToken =>
		   {
			   Username => "$userid",
			   Password => "$passwd"
		   },
		   ServiceAccessToken =>
		   {
			   AccessLicenseNumber => "$access"
		   }
	  	} 	
		Request =>
		{
			RequestOption => 'nonvalidate'
		},
	
		Shipment =>
		{
			Description => 'Shipment Description',
			Shipper =>
			{
				Name => 'ShipperName',
				AttentionName => 'Shipper Attn Name',
				TaxIdentificationNumber => '123456',
				ShipperNumber => '',
				Address =>
				{
					AddressLine => 'Street Name',
					City =>'City Name',
					StateProvinceCode => 'State or Province Code',
					PostalCode => 'Postal Code',
					CountryCode => 'Country Code'
				},
			
				Phone =>
				{
					Number => '1234567890',
					Extension => '1'
				}
			},
			
			ShipTo =>
			{
				Name => 'Ship To Name',
				AttentionName => 'Ship To Attention Name',
				Address =>
				{
					AddressLine => 'Street Name',
					City => 'City Name',
					PostalCode => 'Postal Code',
					CountryCode => 'Country Code'
				},
				
				Phone =>
				{
					Number => '1234567890'
				}
			},
			
			ShipFrom =>
			{
				Name => 'Ship From Name',
				AttentionName => 'Ship From Attention Name',
				Address =>
				{
					AddressLine => 'Street Name',
					City => 'City Name',
					StateProvinceCode => 'State or Province Name',
					PostalCode => 'Postal Code',
					CountryCode => 'Country Code'
				},
				
				Phone =>
				{
					Number => '1234567890'
				}
			},
			
			PaymentInformation =>
			{
				ShipmentCharge =>
				{
					Type => '01',
					BillShipper =>
					{
						CreditCard =>
						{
							Type => 'Shipper Credit Card Type Code',
							Number => 'Shipper Credit Cart Number',
							SecurityCode => 'Shipper Credit Card Security Code',
							ExpirationDate => '12/2013',
							Address =>
							{
								AddressLine => 'Street Name',
								City => 'City Name',
								StateProvinceCode => 'State or Province Code',
								PostalCode => 'Postal Code',
								CountryCode => 'Country Code'
							},
						},
					},
				},
			},
			
			Service =>
			{
				Code => '08',
				Description => 'Express'
			},
			
			ShipmentServiceOptions =>
			{
				
				InternationalForms =>
				{
					FormType => '01',
					InvoiceNumber => 'asdf123',
					InvoiceDate => '20151225',
					PurchaseOrderNumber => '999jjj777',
					TermsOfShipment => 'CFR',
					ReasonForExport => 'Sale',
					Comments => 'Your Comments',
					DeclarationStatement => 'Your Declaration Statement',
					CurrencyCode => 'USD',
					Contacts =>
					{
						SoldTo =>
						{
							Option => '01',
							AttentionName => 'Sold To Attn Name',
							Name => 'Sold To Name',
							Phone =>
							{
								Number => '1234567890',
								Extension => '1234',
							},
							Address =>
							{
								AddressLine => 'SoldTo Street Name',
								City => 'SoldTo City',
								PostalCode => 'Postal Code',
								CountryCode => 'Country Code',
							},
						},
					},
					Product =>
					{
						Description => 'Product 1',
						CommodityCode => 'Commodity Code',
						OriginCountryCode => 'Orign Country Code',
						Unit =>
						{
							Number => '147',
							Value => '478',
							UnitOfMeasurement =>
							{
								Code => 'BOX',
								Description => 'BOX',
							},
						},
						ProductWeight =>
						{
							Weight => '10',
							UnitOfMeasurement =>
							{
								Code => 'LBS',
								Description => 'LBS',
							},
						},
					},
					Discount =>
					{
						MonetaryValue => '100',
					},
					FreightCharges =>
					{
						MonetaryValue => '50',
					},
					InsuranceCharges =>
					{
						MonetaryValue => '200',
					},
					OtherCharges =>
					{
						MonetaryValue => '50',
						Description => 'Misc',
					},
					
				},
			},
			
			Package =>
			{
				Description => '',
				Packaging =>
				{
					Code => '02',
					Description => 'Nails'
				},
				Dimensions =>
				{
					UnitOfMeasurement =>
					{
						Code => 'IN',
						Description =>'Inches'
					},
					
					Length => '7',
					Width => '5',
					Height => '2'
				},
				PackageWeight =>
				{
					UnitOfMeasurement =>
					{
						Code => 'LBS',
						Description => 'Pounds'
					},
					Weight => '10'
				}
			},
 		}
 		LabelSpecification =>
 		{
 			LabelImageFormat =>
 			{
 				Code => 'GIF',
 				Description => 'GIF'
 			},
 			HTTPUserAgent => 'Mozilla/4.5'
 		}
 	};
 	
 	return $request;
 }
 sub processShipConfirm
 {
 	#add soap ship confirm request
 }
 sub processShipAccept
 {
 	#add soap ship accept request
 }
 my $wsdl = XML::Compile::WSDL11->new( $wsdlfile );
 my @schemas = glob "$schemadir/*.xsd";
 $wsdl->importDefinitions(\@schemas);
 my $operation = $wsdl->operation($operation);
 my $call = $operation->compileClient(endpoint => $endpointurl);
 
 if($operation->name() eq "ProcessShipment")
 {
 	($answer , $trace) = $call->(processShipment() , 'UTF-8');	
 }
 elsif($operation->name() eq "ProcessShipConfirm")
 {
 	($answer , $trace) = $call->(processShipConfirm() , 'UTF-8');
 }
 else
 {
 	($answer , $trace) = $call->(processShipAccept() , 'UTF-8');
 }
 
 
 if($answer->{Fault})
 {
	print $answer->{Fault}->{faultstring} ."\n";
	print Dumper($answer);
	print "See XOLTResult.xml for details.\n";
		
	# Save Soap Request and Response Details
	open(fw,">$outputFileName");
	$trace->printRequest(\*fw);
	$trace->printResponse(\*fw);
	close(fw);
 }
 else
 {
	# Get Response Status Description
    print "Description: " . $answer->{Body}->{Response}->{ResponseStatus}->{Description} . "\n"; 
        
    # Print Request and Response
    my $req = $trace->request();
 	print "Request: \n" . $req->content() . "\n";
	my $resp = $trace->response();
	print "Response: \n" . $resp->content();
		
	# Save Soap Request and Response Details
	open(fw,">$outputFileName");
	$trace->printRequest(\*fw);
	$trace->printResponse(\*fw);
	close(fw);
}
 