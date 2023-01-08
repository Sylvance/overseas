import xml.etree.ElementTree as ET
from zeep import Client, Settings
from zeep.exceptions import Fault, TransportError, XMLSyntaxError

# Set Connection
settings = Settings(strict=False, xml_huge_tree=True)
client = Client('SCHEMA-WSDLs/Void.wsdl', settings=settings)

# Set SOAP Headers
headers = {

    'UPSSecurity': {
        'UsernameToken': {
            'Username': 'Your User name',
            'Password': 'Your Password'
        },

        'ServiceAccessToken': {
            'AccessLicenseNumber': 'your access license number'
        }

    }
}

# Create Void Shipment Dictionary
voidShipmentDictionary = {
    'ShipmentIdentificationNumber': 'Your Shipment Identification Number'
}

# Create Request Dictionary
requestDictionary = {

    'RequestOption': 'Request Option'
}

# Try operation
try:
    client.service.ProcessVoid(_soapheaders=headers, Request=requestDictionary, VoidShipment=voidShipmentDictionary)

except Fault as error:
    print(ET.tostring(error.detail))
