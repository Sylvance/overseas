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
 $operation = "ProcessVoid";
 $endpointurl = " Add URL Here";
 $wsdlfile = " Add Wsdl File Here ";
 $schemadir = "Add Schema Location Here";
 $outputFileName = "XOLTResult.xml";
 
 sub processVoid
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
	  	},
		
		
		Request =>
		{
			TransactionReference =>
			{
				CustomerContext => 'Add description here'
			}
		},

		VoidShipment = >
		{
			ShipmentIdentificationNumber => '1Z1234560312345678'
		}
		
 	};
 	
 	return $request;
 }
 
 my $wsdl = XML::Compile::WSDL11->new( $wsdlfile );
 my @schemas = glob "$schemadir/*.xsd";
 $wsdl->importDefinitions(\@schemas);
 my $operation = $wsdl->operation($operation);
 my $call = $operation->compileClient(endpoint => $endpointurl);
 #print $wsdl->explain('ProcessVoid' , PERL => 'INPUT' , recurse => 1);
 
 ($answer , $trace) = $call->(processVoid() , 'UTF-8');	
 
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
 