import xml.etree.ElementTree as ET
import http.client

#Create AccessRequest XML
_AccessLicenseNumber ='Your AccessLicenseNumber'
_userId = 'Your userId'
_password = 'Your password'

AccessRequest = ET.Element('AccessRequest')
ET.SubElement(AccessRequest, 'AccessLicenseNumber').text = _AccessLicenseNumber
ET.SubElement(AccessRequest, 'UserId').text = _userId
ET.SubElement(AccessRequest, 'Password').text = _password

#Create RateRequest XMl
RatingServiceSelectionRequest = ET.Element('RatingServiceSelectionRequest')
Request = ET.SubElement(RatingServiceSelectionRequest, 'Request')
ET.SubElement(Request, 'RequestAction').text = 'Rate'
ET.SubElement(Request, 'RequestOption').text = 'Rate'

shipment = ET.SubElement(RatingServiceSelectionRequest, 'Shipment')
shipper = ET.SubElement(shipment, 'Shipper')
ET.SubElement(shipper, 'Name').text = 'Shipper Name'
ET.SubElement(shipper, 'ShipperNumber').text = 'Your Shipper Number'
shipperAddress = ET.SubElement(shipper, 'Address')
ET.SubElement(shipperAddress, 'AddressLine1').text = 'Address Line 1'
ET.SubElement(shipperAddress, 'City').text = 'Timonium'
ET.SubElement(shipperAddress, 'StateProvinceCode').text = 'MD'
ET.SubElement(shipperAddress, 'CountryCode').text = 'US'

shipTo = ET.SubElement(shipment, 'ShipTo')
ET.SubElement(shipTo, 'CompanyName').text = 'Company Name'
shipToAddress = ET.SubElement(shipTo, 'Address')
ET.SubElement(shipToAddress, 'AddressLine1').text = 'Address line 1'
ET.SubElement(shipToAddress, 'City').text = 'Vero Beach'
ET.SubElement(shipToAddress, 'PostalCode').text = 'PostalCode'
ET.SubElement(shipToAddress, 'CountryCode').text = 'US'

shipFrom = ET.SubElement(shipment, 'ShipFrom')
ET.SubElement(shipFrom, 'CompanyName').text = 'Company Name'
shipFromAddress = ET.SubElement(shipFrom, 'Address')
ET.SubElement(shipFromAddress, 'AddressLine1').text = 'Address line 1'
ET.SubElement(shipFromAddress, 'City').text = 'Vero Beach'
ET.SubElement(shipFromAddress, 'StateProvinceCode').text = 'FL'
ET.SubElement(shipFromAddress, 'PostalCode').text = 'PostalCode'
ET.SubElement(shipFromAddress, 'CountryCode').text = 'US'

service = ET.SubElement(shipment, 'Service')
ET.SubElement(service, 'Code').text = '02'
ET.SubElement(service, 'Description').text = ''

package = ET.SubElement(shipment, 'Package')
packagingType = ET.SubElement(package, 'PackagingType')
ET.SubElement(packagingType, 'Code').text = '02'
ET.SubElement(packagingType, 'Description').text = ''
packageWeight = ET.SubElement(package, 'PackageWeight')
unitOfMeasurement = ET.SubElement(packageWeight, 'unitOfMeasurement')
ET.SubElement(unitOfMeasurement, 'code').text = 'LBS'
ET.SubElement(unitOfMeasurement, 'Weight').text = '60'

#Concatnate xml
_XmlRequest = ET.tostring(AccessRequest)+ET.tostring(RatingServiceSelectionRequest)
print(_XmlRequest)

#Connection
_host = 'Host URL'
_endpointurl = 'Endpoint URL'
conn = http.client.HTTPConnection(_host, port="Port")
conn.request('POST', url=_endpointurl, body=_XmlRequest)
res = conn.getresponse()
print(res.read())


