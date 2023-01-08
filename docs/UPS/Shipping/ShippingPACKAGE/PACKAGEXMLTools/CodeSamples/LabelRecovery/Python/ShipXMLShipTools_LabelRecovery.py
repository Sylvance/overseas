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

#Create LabelRecoveryRequest XML
labelRecoveryRequest = ET.Element('LabelRecoveryRequest')
request = ET.SubElement(labelRecoveryRequest, 'Request')
ET.SubElement(request, 'RequestAction').text = 'LabelRecovery'

labelSpecification = ET.SubElement(labelRecoveryRequest, 'LabelSpecification')
ET.SubElement(labelSpecification, 'HTTPUserAgent').text = 'HTTPUserAgent'
labelImageFormat = ET.SubElement(labelSpecification, 'LabelImageFormat')
ET.SubElement(labelImageFormat, 'Code').text = 'GIF'

labelDelivery = ET.SubElement(labelRecoveryRequest, 'LabelDelivery')
ET.SubElement(labelDelivery, 'LabelLinkIndicator')
ET.SubElement(labelDelivery, 'ResendEMailIndicator')

ET.SubElement(labelRecoveryRequest, 'TrackingNumber').text = 'Your Tracking Number'

#Concatnate xml
_XmlRequest = ET.tostring(AccessRequest)+ET.tostring(labelRecoveryRequest)
print(_XmlRequest)

#Connection
_host = 'Host URL'
_endpointurl = 'Endpoint URL'
conn = http.client.HTTPConnection(_host, port="Port")
conn.request('POST', url=_endpointurl, body=_XmlRequest)
res = conn.getresponse()
print(res.read())


