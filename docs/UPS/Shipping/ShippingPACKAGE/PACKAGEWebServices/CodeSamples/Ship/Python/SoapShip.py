import xml.etree.ElementTree as ET
from zeep import Client, Settings
from zeep.exceptions import Fault, TransportError, XMLSyntaxError

# Set Connection
settings = Settings(strict=False, xml_huge_tree=True)
client = Client('SCHEMA-WSDLs/Ship.wsdl', settings=settings)

# Set SOAP Headers
headers = {

    'UPSSecurity': {
        'UsernameToken': {
            'Username': 'Your Username',
            'Password': 'Your Password'
        },

        'ServiceAccessToken': {
            'AccessLicenseNumber': 'Your Access License'
        }

    }
}

# Create request dictionary
requestDictionary = {

    "RequestOption": "nonvalidate",
    "TransactionReference": {
        "CustomerContext": "Customer Context"
    }
}

# Create Ship request dictionary
shipmentRequestDictionary = {

    "Description": "Ship WS test",
    "Package": {
        "Description": '',
        "Dimensions": {
            "Height": "2",
            "Length": "7",
            "UnitOfMeasurement": {
                "Code": "IN",
                "Description": "Inches"
            },
            "Width": "5"
        },
        "PackageWeight": {
            "UnitOfMeasurement": {
                "Code": "LBS",
                "Description": "Pounds"
            },
            "Weight": "10"
        },
        "Packaging": {
            "Code": "02",
            "Description": ""
        }
    },
    "PaymentInformation": {
        "ShipmentCharge": {
            "BillShipper": {
                "AccountNumber": "Account Number"
            },
            "Type": "01"
        }
    },
    "Service": {
        "Code": "03",
        "Description": ""
    },
    "ShipFrom": {
        "Address": {
            "AddressLine": "Street name",
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "Sate Code"
        },
        "AttentionName": "",
        "FaxNumber": "1234567890",
        "Name": "",
        "Phone": {
            "Number": "1234567890"
        }
    },
    "ShipTo": {
        "Address": {
            "AddressLine": "Street Name",
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "State or province Code"
        },
        "AttentionName": "",
        "Name": "",
        "Phone": {
            "Number": "123456789"
        }
    },
    "Shipper": {
        "Address": {
            "AddressLine": "Street Name",
            "City": "City Name",
            "CountryCode": "Country Code",
            "PostalCode": "Postal Code",
            "StateProvinceCode": "State or Province Code"
        },
        "AttentionName": "",
        "FaxNumber": "123456789",
        "Name": "ShipperName",
        "Phone": {
            "Extension": "1",
            "Number": "123456789"
        },
        "ShipperNumber": "Shipper Number",
        "TaxIdentificationNumber": "123456"
    }

}

# Create label specification dictionary
labelSepcificationDictionary = {

    "HTTPUserAgent": "Mozilla/4.5",
    "LabelImageFormat": {
        "Code": "GIF",
        "Description": "GIF"
    }

}

# Try operation
try:
    response = client.service.ProcessShipment(_soapheaders=headers, Request=requestDictionary,
                                              Shipment=shipmentRequestDictionary,
                                              LabelSpecification=labelSepcificationDictionary)
    print(response)


except Fault as error:
    print(ET.tostring(error.detail))
