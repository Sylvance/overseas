import xml.etree.ElementTree as ET
from zeep import Client, Settings
from zeep.exceptions import Fault, TransportError, XMLSyntaxError

# Set Connection
settings = Settings(strict=False, xml_huge_tree=True)
client = Client('<WSDL Location>/DangerousGoodsUtility.wsdl', settings=settings)

# Set SOAP headers
headers = {
     'UPSSecurity': {
        'UsernameToken': {
            'Username': '<Your username>',
            'Password': '<Your password>'
        },
        'ServiceAccessToken': {
            'AccessLicenseNumber': '<Your accesslicensenumber>'
        }
    }
}

# Create request dictionary

request = {
    "TransactionReference": {
        "CustomerContext": "Audit PreCheck",
        "TransactionIdentifier": "<txn identifier>"
    }

}

originRecordTransactionTimestamp = "2019-07-11T17:50:32.453"

shipment = {
    "ShipperNumber": "<Shipper Number>",
    "ShipFromAddress": {
       "AddressLine": "Piri Reisweg 23",
       "City": "Sevenum",
       "PostalCode": "5975 RZ",
       "CountryCode": "NL"
    },
    "ShipToAddress": {
       "AddressLine": "Chirurgien Dentiste",
       "City": "PARIS",
       "StateProvinceCode":"FR",
       "PostalCode": "75008",
       "CountryCode": "FR"
    },
    "Service": {
        "Code": "11"
    },
    "RegulationSet": "ADR",
    "Package": {
        "PackageIdentifier": "1",
        "PackageWeight": {
            "Weight": "0.345",
            "UnitOfMeasurement": {
                "Code": "KGS"
            }
        },
        "TransportationMode": "GND",
        "EmergencyPhone": "<Sample Phone number>",
        "EmergencyContact": "Hemang",
        "ChemicalRecord": {
            "ChemicalRecordIdentifier": "1",
            "ClassDivisionNumber": "3",
            "IDNumber": "1993",
            "Quantity": "50",
            "UOM": "g",
            "PackagingInstructionCode": "130",
            "ProperShippingName": "Cartridges, small arms, blank",
            "PackagingType": "Fiberboard Box",
            "PackagingTypeQuantity": "1",
            "CommodityRegulatedLevelCode": "LQ"
        }
    }
}
# Try operation
try:

    response = client.service.ProcessAcceptanceAuditPreCheck(_soapheaders=headers, Request=request,
                                                             OriginRecordTransactionTimestamp=originRecordTransactionTimestamp,
                                                             Shipment=shipment)
    print(response)

except Fault as  error:
    print(ET.tostring(error.detail))