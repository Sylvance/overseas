no warnings; # turn off warnings

use XML::Compile::Schema;
use XML::LibXML;
use XML::LibXML::Simple;
use LWP::UserAgent;
use HTTP::Request;
use Data::Dumper;

#Configuration
$access = " Add License Key Here";
$userid = " Add User Id Here";
$passwd = " Add Password Here";

$endpointurl=" Add URL Here";
$accessSchemaFile=" Add AccessRequest Schema File";
$requestSchemaFile=" Add LocatorRequest Schema File";
$responseSchemaFile=" Add LocatorResponse Schema File";
$outputFileName = "XOLTResult.xml";

@XML = (); # Array to hold request

my $schema = XML::Compile::Schema->new("$accessSchemaFile");
#print $schema->template('PERL' => 'AccessRequest');
my $doc = XML::LibXML::Document->new('1.0', 'UTF-8');
my $writer = $schema->compile(WRITER => 'AccessRequest');
my $accessrequest = 
{
    AccessLicenseNumber => "$access" ,
    UserId => "$userid",
    Password => "$passwd"
};

my $xml = $writer->($doc , $accessrequest);
$doc->setDocumentElement($xml);
push(@XML , $doc->toString());

$schema = XML::Compile::Schema->new("$requestSchemaFile");
#print $schema->template('PERL' => 'LocatorRequest');
$doc = XML::LibXML::Document->new('1.0', 'UTF-8');
$writer = $schema->compile(WRITER => 'LocatorRequest');

my $locatorrequest =
{
	Request =>
	{
		RequestAction => 'Locator',
		RequestOption => '3',
		TransactionReference =>
		{
			CustomerContext => 'Your Test Case Summary Description'
		},
	},
	OriginAddress =>
	{
		AddressKeyFormat =>
		{
			AddressLine => '200 warsaw rd',
			PoliticalDivision2 => 'Atlanta',
			PoliticalDivision1 => 'GA',
			PostcodePrimaryLow => '85281',
			PostcodeExtendedLow => '4510',
			CountryCode => 'US'
		},
		MaximumListSize => ''
	},
	Translate =>
	{
		LanguageCode => 'ENG'
	},
	UnitOfMeasurement => 
	{
		Code => 'MI'
	},
	LocationID => '',
	LocationSearchCriteria => 
	{
		SearchOption => 
		[
		   {
			    OptionType =>
			    {
			   	   	 Code => '02'
			    },
			    OptionCode =>
			    [
				     {
				    	Code => '01'
				     },
				     {
				    	Code => '03'
				     },
				     {
				    	Code => '04'
				     },
				     {
				    	Code => '05'
				     },
			    ], 
		   },
		   {
		   	    OptionType =>
		   	    {
		   	    	 Code => '03'
		   	    },
		   	    OptionCode =>
		   	    {
		   	    	 Code => '002'
		   	    }
		   },
		   
		],
		MaximumListSize => '',
		SearchRadius => '',
		ServiceSearch =>
		{
		 	Time => '1600',
		 	ServiceCode =>
		 	[
		 	    {
		 	    	Code => '01'
		 	    },
		 	    {
		 	    	Code => '02'
		 	    },
		 	],
		 	ServiceOptionCode =>
		 	{
		 		Code => '01'
		 	}
		},
		    
	},
	
};

$xml = $writer->($doc , $locatorrequest);
$doc->setDocumentElement($xml);
push(@XML , $doc->toString());

#Send HTTP Request
my $browser = LWP::UserAgent->new();   
my $req = HTTP::Request->new(POST => $endpointurl);
print "Request: \n@XML";
$req->content("@XML");
    
#Get HTTP Response Status
my $resp = $browser->request($req);
print "Response: ";
print $resp->status_line() . "\n";
print $resp->content() . "\n";

#Get Response Status
$parser = XML::LibXML::Simple->new();
my $xmlResp= $parser->XMLin($resp->content());
#print Dumper($xmlResp);
if($xmlResp->{Response}->{ResponseStatusDescription} =~ /success/i)
{
	print $xmlResp->{Response}->{ResponseStatusDescription};
}
else
{
	print Dumper($xmlResp);
}

#Save Response To File
open(fw,">$outputFileName");
print fw $resp->content();
close(fw);