 no warnings;
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
 $operation = "ProcessTimeInTransit";
 $endpointurl = " Add URL Here";
 $wsdlfile = " Add Wsdl File Here ";
 $schemadir = "Add Schema Location Here";
 $outputFileName = "XOLTResult.xml";
 
 my %request = 
 (
	# Header Block

	  UPSSecurity =>  
	  {
		   UsernameToken =>
		   {
			   Username => $userid,
			   Password => $passwd
		   },
		   ServiceAccessToken =>
		   {
			   AccessLicenseNumber => $access
		   }
	  },

	# Body Block

	  Request =>   
	  {
		   RequestOption => 'TNT'
	  },
	  ShipFrom =>
	  {
		 Address =>
		 {
			City => 'Roswell',
			CountryCode => 'US',
			PostalCode => '30076',
			StateProvinceCode => 'GA'
		 }
	  },
	  ShipTo =>
	  {
		 Address =>
		 {
			City => 'Roswell',
			CountryCode => 'US',
			PostalCode => '30076',
			StateProvinceCode => 'GA'
		 }
	  },
	  Pickup =>
	  {
		 Date => '20111030'
	  },
	  ShipmentWeight =>
	  {
		 UnitOfMeasurement =>
		 {
			  Code => 'KGS',
			  Description => 'Kilograms'
		 },
		 Weight => '10'
	  },
	  TotalPackagesInShipment => '1' ,
	  InvoiceLineTotal =>
	  {
		 CurrencyCode => 'CAD' ,
		 MonetaryValue => '10'
	  },
	  MaximumListSize => '1'    		  
);

my $wsdl = XML::Compile::WSDL11->new( $wsdlfile );
my @schemas = glob "$schemadir/*.xsd";
$wsdl->importDefinitions(\@schemas);
my $operation = $wsdl->operation($operation);
my $call = $operation->compileClient(endpoint => $endpointurl);
 
# Call service
my ($answer,$trace) = $call->(\%request , 'UTF-8');

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
