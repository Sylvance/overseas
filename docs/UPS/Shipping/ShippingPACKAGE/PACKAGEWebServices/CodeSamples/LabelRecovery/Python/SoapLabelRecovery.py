import xml.etree.ElementTree as ET
from zeep import Client, Settings
from zeep.exceptions import Fault, TransportError, XMLSyntaxError

#set connetion
settings = Settings(strict=False, xml_huge_tree=True)
client = Client('SCHEMA-WSDLs/LabelRecoveryWS.wsdl', settings=settings)

#set soap headers
headers = {

    'UPSSecurity':{
        'UsernameToken':{
            'Username':'Your User name',
            'Password':'Your Password'
        },

        'ServiceAccessToken' : {
        'AccessLicenseNumber':'your access license number'
        }

    }
}

#create request dictionary
RequestDictionary = {

    'RequestOption': 'Request Option'
}

#try operation
try:
    client.service.ProcessLabelRecovery(_soapheaders=headers,Request=RequestDictionary,TrackingNumber='Your Shipment Tracking Number')
except Fault as error:
    print(ET.tostring(error.detail))
