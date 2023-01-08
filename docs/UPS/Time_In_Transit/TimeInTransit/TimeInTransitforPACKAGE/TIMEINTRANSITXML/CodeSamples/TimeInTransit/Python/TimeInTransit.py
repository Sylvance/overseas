import xml.etree.ElementTree as ET
import requests as req
import sys
_AccessLicenseNumber ='<Your AccessLicense number>'
_userId = '<Your USerid>'
_password = '<Your Password>'

AccessLicenseNumber = bytes(_AccessLicenseNumber, 'utf-8').decode('utf-8', 'ignore')
userId = bytes(_userId, 'utf-8').decode('utf-8', 'ignore')
password = bytes(_password, 'utf-8').decode('utf-8', 'ignore')
AccessRequest = ET.Element('AccessRequest')
ET.SubElement(AccessRequest,'AccessLicenseNumber').text = _AccessLicenseNumber
ET.SubElement(AccessRequest,'UserId').text = _userId
ET.SubElement(AccessRequest,'Password').text = _password


TimeInTransitRequest = ET.Element('TimeInTransitRequest')
Request = ET.SubElement(TimeInTransitRequest,'Request')
TransactionReference = ET.SubElement(Request,'TransactionReference')
ET.SubElement(TransactionReference,'CustomerContext').text = 'Customer context'
ET.SubElement(TransactionReference,'XpciVersion').text = '1.0002'
ET.SubElement(Request,'RequestAction').text = 'TimeInTransit'


TransitFrom = ET.SubElement(TimeInTransitRequest,'TransitFrom')

AddressArtifactFormat = ET.SubElement(TransitFrom,'AddressArtifactFormat')
ET.SubElement(AddressArtifactFormat,'PoliticalDivision2').text = 'Carrickmacross'

ET.SubElement(AddressArtifactFormat,'CountryCode').text = 'IE'


TransitTo = ET.SubElement(TimeInTransitRequest,'TransitTo')

AddressArtifactFormat1 = ET.SubElement(TransitTo,'AddressArtifactFormat')
ET.SubElement(AddressArtifactFormat1,'PoliticalDivision2').text = 'Stockholm'
ET.SubElement(AddressArtifactFormat1,'PoliticalDivision1').text = 'SE'
ET.SubElement(AddressArtifactFormat1,'PostcodePrimaryLow').text = '11187'
ET.SubElement(AddressArtifactFormat1,'CountryCode').text = 'US'

#PickupDate = ET.SubElement(TimeInTransitRequest,'PickupDate')
ET.SubElement(TimeInTransitRequest,'PickupDate').text='20191128'

#MaximumListSize = ET.SubElement(TimeInTransitRequest,'MaximumListSize')
ET.SubElement(TimeInTransitRequest,'MaximumListSize').text='10'

InvoiceLineTotal = ET.SubElement(TimeInTransitRequest,'InvoiceLineTotal')
#CurrencyCode = ET.SubElement(InvoiceLineTotal,'CurrencyCode')
ET.SubElement(InvoiceLineTotal,'CurrencyCode').text='EUR'
#MonetaryValue = ET.SubElement(InvoiceLineTotal,'MonetaryValue')
ET.SubElement(InvoiceLineTotal,'MonetaryValue').text='100'


ShipmentWeight = ET.SubElement(TimeInTransitRequest,'ShipmentWeight')
UnitOfMeasurement = ET.SubElement(ShipmentWeight,'UnitOfMeasurement')
#Code = ET.SubElement(UnitOfMeasurement,'Code')
ET.SubElement(UnitOfMeasurement,'Code').text='KGS'
#Weight = ET.SubElement(ShipmentWeight,'Weight')
ET.SubElement(ShipmentWeight,'Weight').text='5.0'



_reqString = ET.tostring(AccessRequest)
print(_reqString)

tree = ET.ElementTree(ET.fromstring(_reqString))
root = tree.getroot()

_TNTRequest = ET.tostring(TimeInTransitRequest)
tntTree = ET.ElementTree(ET.fromstring(_TNTRequest))
tntRoot = tntTree.getroot()


_XmlRequest = ET.tostring(root,encoding='utf8', method='xml') +ET.tostring(tntRoot,encoding='utf8', method='xml')
_XmlRequest = _XmlRequest.decode().replace('\n','')
print(_XmlRequest)
_host='https://onlinetools.ups.com/ups.app/xml/TimeInTransit'
res = req.post(_host,_XmlRequest)
print(res.content)

