import xml.etree.ElementTree as ET
import requests as req
import sys

_AccessLicenseNumber ='<Your access License Number>'
_userId = '<Userid>'
_password = '<Password>'

AccessLicenseNumber = bytes(_AccessLicenseNumber, 'utf-8').decode('utf-8', 'ignore')
userId = bytes(_userId, 'utf-8').decode('utf-8', 'ignore')
password = bytes(_password, 'utf-8').decode('utf-8', 'ignore')
AccessRequest = ET.Element('AccessRequest')
ET.SubElement(AccessRequest,'AccessLicenseNumber').text = _AccessLicenseNumber
ET.SubElement(AccessRequest,'UserId').text = _userId
ET.SubElement(AccessRequest,'Password').text = _password

LocatorRequest = ET.Element('LocatorRequest')
Request = ET.SubElement(LocatorRequest,'Request')
ET.SubElement(Request,'RequestAction').text = 'Locator'
ET.SubElement(Request,'RequestOption').text = '2'


OriginAddress = ET.SubElement(LocatorRequest,'OriginAddress')
Geocode = ET.SubElement(OriginAddress,'Geocode')
ET.SubElement(Geocode,'Latitude').text = '33.48671722'
ET.SubElement(Geocode,'Longitude').text = '112.125671'

AddressKeyFormat = ET.SubElement(OriginAddress,'AddressKeyFormat')
ET.SubElement(AddressKeyFormat,'AddressLine').text = ''
ET.SubElement(AddressKeyFormat,'PoliticalDivision2').text = ''
ET.SubElement(AddressKeyFormat,'PoliticalDivision1').text = ''
ET.SubElement(AddressKeyFormat,'PostcodePrimaryLow').text = '30005'
ET.SubElement(AddressKeyFormat,'PostcodeExtendedLow').text = ''
ET.SubElement(AddressKeyFormat,'CountryCode').text = 'US'

Translate = ET.SubElement(LocatorRequest,'Translate')
ET.SubElement(Translate,'LanguageCode').text = 'ENG'

UnitOfMeasurement = ET.SubElement(LocatorRequest,'UnitOfMeasurement')
ET.SubElement(UnitOfMeasurement,'Code').text = 'MI'

ET.SubElement(LocatorRequest,'LocationID').text = '49249'

_reqString = ET.tostring(AccessRequest)
print(_reqString)

tree = ET.ElementTree(ET.fromstring(_reqString))
root = tree.getroot()

_QuantunmRequest = ET.tostring(LocatorRequest)
quantunmTree = ET.ElementTree(ET.fromstring(_QuantunmRequest))
quantRoot = quantunmTree.getroot()


_XmlRequest = ET.tostring(root,encoding='utf8', method='xml') +ET.tostring(quantRoot,encoding='utf8', method='xml')
_XmlRequest = _XmlRequest.decode().replace('\n','')
print(_XmlRequest)
_host='http://onlinetools.ups.com/ups.app/xml/Locator'
res = req.post(_host,_XmlRequest)
print(res.content)

