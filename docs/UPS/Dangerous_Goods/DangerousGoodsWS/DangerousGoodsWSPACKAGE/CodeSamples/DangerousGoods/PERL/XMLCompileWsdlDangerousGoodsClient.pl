 no warnings;
 use XML::Compile::WSDL11;
 use XML::Compile::SOAP11;
 use XML::Compile::Transport::SOAPHTTP;
 use HTTP::Request;
 use HTTP::Response;
 use Data::Dumper;
 
 #Configuration
 $access = "Set your Access Key here.";
 $userid = "Set your User Id here.";
 $passwd = "Set your Password here.";
 $operation = "ProcessChemicalReferenceData"; # Set the value of operation to either ProcessChemicalReferenceData or ProcessAcceptanceAuditPreCheck.
 $endpointurl = "https://wwwcie.ups.com/webservices/DangerousGoodsUtility or  https://onlinetools.ups.com/webservices/DangerousGoodsUtility";
 $wsdlfile = "Set Your WSDL Home here.";
 $schemadir = "Set Your Schema Directory here.";
 $outputFileName = "XOLTResult.xml";
 
 
 sub processChemicalReferenceData
 {
	 my $request =
	 {
	 	  UPSSecurity =>  
		  {
			  UsernameToken =>
			  {
					Username => $userid,
					Password => $passwd,
			  },
			  
			  ServiceAccessToken =>
			  {
				    AccessLicenseNumber => $access,
			  },
		  },
		  
		  Request =>
		  {
		  	   TransactionReference =>
		  	   {
		  	   	   CustomerContext => 'PERL Test Client Program for Chemical Reference Data Request.',
		  	   },
		  },
		  
		  ShipperNumber => 'Set your Shipper Number here.',
		  
		  ProperShippingName => 'UN2561',
		  
		  IDNumber => '3-METHYL-1-BUTENE',  
	 };
	 
	 return $request
 }
 
 sub processAcceptanceAuditPreCheck
 {
 	 my $request =
	 {
	 	  UPSSecurity =>  
		  {
			  UsernameToken =>
			  {
					Username => $userid,
					Password => $passwd,
			  },
			  
			  ServiceAccessToken =>
			  {
				    AccessLicenseNumber => $access,
			  },
		  },
		  
		  Request =>
		  {
		  	   TransactionReference =>
		  	   {
		  	   	   CustomerContext => 'PERL Test Client Program for Acceptance Audit Pre-Check Request.',
		  	   },
		  	   
		  },
		  
		  Shipment => 
		  {
				ShipperNumber => 'Set your Shipper Number here.',
				
				ShipFromAddress =>
				{
					AddressLine => "Clayallee 170",
					City => "Berlin",
					PostalCode => "14191",
					CountryCode => "DE",
				},
				
				ShipToAddress =>
				{
					AddressLine => "Gieener Str",
					City => "Frankfurt",
					PostalCode => "60435",
					CountryCode => "DE",
				},
				
				Service =>
				{
					Code => "011",
				},
				
				RegulationSet => 'ADR',
				
				Package =>
				{
					PackageIdentifier => "1",
					EmergencyPhone => "1234567890",
					EmergencyContact => "Emergency Contact",
					TransportationMode => "GND",
					
					PackageWeight =>
					{
						Weight => "1",
						
						UnitOfMeasurement =>
						{
							Code => "KGS",
						}
					},
					
					ChemicalRecord =>
					{
						ChemicalRecordIdentifier => "1",
						ReportableQuantity => "RQ",
						ClassDivisionNumber => "3",
						IDNumber => "UN2561",
						PackagingGroupType => "I",
						Quantity => "1",
						UOM => "L",
						PackagingInstructionCode => "P001",
						ProperShippingName => "3-METHYL-1-BUTENE",
						PackagingType => "Set your Packaging Type here.",
						PackagingTypeQuantity => "1",
						CommodityRegulatedLevelCode => "FR",
						TransportCategory => "1",
						TunnelRestrictionCode => "D/E",
					},
				},
		  },
		  
	 };
	 
	 return $request
 }
 
 
 my $wsdl = XML::Compile::WSDL11->new( $wsdlfile );
 my @schemas = glob "$schemadir/*.xsd";
 $wsdl->importDefinitions(\@schemas);
 my $operation = $wsdl->operation($operation);
 my $call = $operation->compileClient( endpoint => $endpointurl );
 
 
 if($operation->name() eq "ProcessChemicalReferenceData")
 {
 	($answer,$trace) = $call->(processChemicalReferenceData() , 'UTF-8');
 }
 elsif($operation->name() eq "ProcessAcceptanceAuditPreCheck")
 {
 	($answer,$trace) = $call->(processAcceptanceAuditPreCheck() , 'UTF-8');
 }
 else
 {
 	print "Invalid operation\n";
 }

 if($answer->{Fault})
 {
	print $answer->{Fault}->{faultstring} ."\n";
	print Dumper($answer);
	print "See XOLTResult.xml for details.\n";
		
	# Save Soap Request and Response Details
	open(fw,">XOLTResult.xml");
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
	open(fw,">XOLTResult.xml");
	$trace->printRequest(\*fw);
	$trace->printResponse(\*fw);
	close(fw);
}

