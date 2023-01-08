import xml.etree.ElementTree as ET
from zeep import Client, Settings
from zeep.exceptions import Fault, TransportError, XMLSyntaxError

# Set Connection
settings = Settings(strict=False, xml_huge_tree=True)
client = Client('SCHEMA-WSDLs/RateWS.wsdl', settings=settings)

# Set SOAP headers
headers = {

    'UPSSecurity': {
        'UsernameToken': {
            'Username': 'Your Username',
            'Password': 'Your Password'
        },

        'ServiceAccessToken': {
            'AccessLicenseNumber': 'You AccessLicense Number'
        }

    }
}

# Create request dictionary
requestDictionary = {

    "RequestOption": "Shop",
    "TransactionReference": {
        "CustomerContext": "Your Customer Context"
    }
}

# Create rate request dictionary
rateRequestDictionary = {

    "Package": {
        "Dimensions": {
            "Height": "10",
            "Length": "5",
            "UnitOfMeasurement": {
                "Code": "IN",
                "Description": "inches"
            },
            "Width": "4"
        },
        "PackageWeight": {
            "UnitOfMeasurement": {
                "Code": "Lbs",
                "Description": "pounds"
            },
            "Weight": "1"
        },
        "PackagingType": {
            "Code": "02",
            "Description": "Rate"
        }
    },
    "Service": {
        "Code": "03",
        "Description": "Service Code"
    },
    "ShipFrom": {
        "Address": {
            "AddressLine": [
                "Street name",
            ],
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "State or Province Code"
        },
        "Name": "Name"
    },
    "ShipTo": {
        "Address": {
            "AddressLine": "Street Name",
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "State or Province Code"
        },
        "Name": "Name"
    },
    "Shipper": {
        "Address": {
            "AddressLine": [
                "Street Name",
            ],
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "State or Province Code"
        },
        "Name": "Name",
        "ShipperNumber": "Shipper Number"
    }
}

# Try operation
try:
    response = client.service.ProcessRate(_soapheaders=headers, Request=requestDictionary,
                                          Shipment=rateRequestDictionary)
    print(response)

except Fault as error:
    print(ET.tostring(error.detail))
