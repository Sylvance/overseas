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

#Create VoidShipmentRequest XML
VoidShipmentRequest = ET.Element('VoidShipmentRequest')
request = ET.SubElement(VoidShipmentRequest, 'Request')
ET.SubElement(request, 'RequestAction').text = '1'
ET.SubElement(VoidShipmentRequest, 'ShipmentIdentificationNumber').text = 'Your Shipment Identification Number'

#Concatnate xml
_XmlRequest = ET.tostring(AccessRequest)+ET.tostring(VoidShipmentRequest)
print(_XmlRequest)

#Connection
_host = 'Host URL'
_endpointurl = 'Endpoint URL'
conn = http.client.HTTPConnection(_host, port="Port")
conn.request('POST', url=_endpointurl, body=_XmlRequest)
res = conn.getresponse()
print(res.read())


