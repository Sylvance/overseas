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

#Create ShipmentConfirmRequest XMl
ShipmentConfirmRequest = ET.Element('ShipmentConfirmRequest')
Request = ET.SubElement(ShipmentConfirmRequest, 'Request')
ET.SubElement(Request, 'RequestAction').text = 'ShipConfirm'
ET.SubElement(Request, 'RequestOption').text = 'nonvalidate'

labelSpecification = ET.SubElement(ShipmentConfirmRequest, 'LabelSpecification')
ET.SubElement(labelSpecification, 'HTTPUserAgent').text = ''
labelPrintMethod = ET.SubElement(labelSpecification, 'LabelPrintMethod')
ET.SubElement(labelPrintMethod, 'Code').text = 'GIF'
ET.SubElement(labelPrintMethod, 'Description').text = ''
labelImageFormat = ET.SubElement(labelSpecification, 'LabelImageFormat')
ET.SubElement(labelImageFormat, 'Code').text = 'GIF'
ET.SubElement(labelImageFormat, 'Description').text = ''

shipment = ET.SubElement(ShipmentConfirmRequest, 'Shipment')
ET.SubElement(shipment, 'Description').text = ''
rateInformation = ET.SubElement(shipment, 'RateInformation')
ET.SubElement(rateInformation, 'NegotiatedRatesIndicator').text = ''

shipper = ET.SubElement(shipment, 'Shipper')
ET.SubElement(shipper, 'Name').text = 'Shipper Name'
ET.SubElement(shipper, 'PhoneNumber').text = '1234567890'
ET.SubElement(shipper, 'TaxIdentificationNumber').text = '1234567877'
ET.SubElement(shipper, 'ShipperNumber').text = 'Your Shipper Number'
shipperAddress = ET.SubElement(shipper, 'Address')
ET.SubElement(shipperAddress, 'AddressLine1').text = 'Address Line 1'
ET.SubElement(shipperAddress, 'City').text = 'Timonium'
ET.SubElement(shipperAddress, 'StateProvinceCode').text = 'MD'
ET.SubElement(shipperAddress, 'PostalCode').text = 'Postal Code'
ET.SubElement(shipperAddress, 'CountryCode').text = 'US'

shipTo = ET.SubElement(shipment, 'ShipTo')
ET.SubElement(shipTo, 'CompanyName').text = 'Company Name'
ET.SubElement(shipTo, 'AttentionName').text = 'Ship to attention name'
ET.SubElement(shipTo, 'PhoneNumber').text = '1234567890'
shipToAddress = ET.SubElement(shipTo, 'Address')
ET.SubElement(shipToAddress, 'AddressLine1').text = 'Address line 1'
ET.SubElement(shipToAddress, 'City').text = 'Vero Beach'
ET.SubElement(shipToAddress, 'StateProvinceCode').text = 'FL'
ET.SubElement(shipToAddress, 'PostalCode').text = 'PostalCode'
ET.SubElement(shipToAddress, 'CountryCode').text = 'US'

shipFrom = ET.SubElement(shipment, 'ShipFrom')
ET.SubElement(shipFrom, 'CompanyName').text = 'Company Name'
ET.SubElement(shipFrom, 'AttentionName').text = 'Ship From attention name'
ET.SubElement(shipFrom, 'PhoneNumber').text = '1234567890'
ET.SubElement(shipFrom, 'TaxIdentificationNumber').text = '1234567877'
shipFromAddress = ET.SubElement(shipFrom, 'Address')
ET.SubElement(shipFromAddress, 'AddressLine1').text = 'Address line 1'
ET.SubElement(shipFromAddress, 'City').text = 'Vero Beach'
ET.SubElement(shipFromAddress, 'StateProvinceCode').text = 'FL'
ET.SubElement(shipFromAddress, 'PostalCode').text = 'PostalCode'
ET.SubElement(shipFromAddress, 'CountryCode').text = 'US'

paymentInformation = ET.SubElement(shipment, 'PaymentInformation')
prepaid = ET.SubElement(paymentInformation, 'Prepaid')
billShipper = ET.SubElement(prepaid, 'BillShipper')
ET.SubElement(billShipper, 'AccountNumber').text = 'Your Account Number'

service = ET.SubElement(shipment, 'Service')
ET.SubElement(service, 'Code').text = '02'
ET.SubElement(service, 'Description').text = ''

package = ET.SubElement(shipment, 'Package')
ET.SubElement(package, 'Description').text = ''
packagingType = ET.SubElement(package, 'PackagingType')
ET.SubElement(packagingType, 'Code').text = '02'
ET.SubElement(packagingType, 'Description').text = ''
packageWeight = ET.SubElement(package, 'PackageWeight')
ET.SubElement(packageWeight, 'Weight').text = '60'
ET.SubElement(packageWeight, 'UnitOfMeasurement')

#Concatnate xml
_XmlRequest = ET.tostring(AccessRequest)+ET.tostring(ShipmentConfirmRequest)

#Connection
_host = 'Host URL'
_endpointurl = 'Endpoint URL'
conn = http.client.HTTPConnection(_host, port="Port")
conn.request('POST', url=_endpointurl, body=_XmlRequest)
res = conn.getresponse()
print(res.read())


