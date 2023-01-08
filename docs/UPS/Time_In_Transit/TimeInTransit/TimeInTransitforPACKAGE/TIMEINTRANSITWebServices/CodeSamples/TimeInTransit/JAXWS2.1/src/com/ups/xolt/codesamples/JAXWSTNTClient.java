/* 
 ** 
 ** Filename: JAXWSTNTClient.java 
  ** Authors: United Parcel Service of America
 ** 
 ** The use, disclosure, reproduction, modification, transfer, or transmittal 
 ** of this work for any purpose in any form or by any means without the 
 ** written permission of United Parcel Service is strictly prohibited. 
 ** 
 ** Confidential, Unpublished Property of United Parcel Service. 
 ** Use and Distribution Limited Solely to Authorized Personnel. 
 ** 
 ** Copyright 2009 United Parcel Service of America, Inc.  All Rights Reserved. 
 ** 
 */
package com.ups.xolt.codesamples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import com.ups.wsdl.xoltws.tnt.v1.TimeInTransitErrorMessage;
import com.ups.wsdl.xoltws.tnt.v1.TimeInTransitPortType;
import com.ups.wsdl.xoltws.tnt.v1.TimeInTransitService;
import com.ups.xmlschema.xoltws.tnt.v1.CodeDescriptionType;
import com.ups.xmlschema.xoltws.common.v1.RequestType;
import com.ups.xmlschema.xoltws.error.v1.Errors;
import com.ups.xmlschema.xoltws.tnt.v1.InvoiceLineTotalType;
import com.ups.xmlschema.xoltws.tnt.v1.PickupType;
import com.ups.xmlschema.xoltws.tnt.v1.RequestShipFromAddressType;
import com.ups.xmlschema.xoltws.tnt.v1.RequestShipFromType;
import com.ups.xmlschema.xoltws.tnt.v1.RequestShipToAddressType;
import com.ups.xmlschema.xoltws.tnt.v1.RequestShipToType;
import com.ups.xmlschema.xoltws.tnt.v1.ShipmentWeightType;
import com.ups.xmlschema.xoltws.tnt.v1.TimeInTransitRequest;
import com.ups.xmlschema.xoltws.tnt.v1.TimeInTransitResponse;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity.ServiceAccessToken;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity.UsernameToken;

public class JAXWSTNTClient {

	private static String accesskey;
	private static String username;
	private static String password;
	private static String out_file_location = "out_file_location";
	private static String tool_or_webservice_name = "tool_or_webservice_name";
	private static final String endpoint_url = "url";
	static Properties props = null;
	
	static{

        try{
        	props = new Properties();
        	props.load(new FileInputStream("./build.properties"));
	  		accesskey = props.getProperty("accesskey");
	  		username = props.getProperty("username");
	  		password = props.getProperty("password");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}

	public static void main(String args[])throws Exception {
		String statusCode = null;
		String description = null;
    try {
    		
    	TimeInTransitService tntService = new TimeInTransitService();
    	TimeInTransitPortType tntPort = tntService.getTimeInTransitPort();
    	BindingProvider bp = (BindingProvider)tntPort;
    	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, props.getProperty(endpoint_url));
		TimeInTransitRequest tntRequest = new TimeInTransitRequest();
		RequestType reqType = new RequestType();
		List<String> requestOption = reqType.getRequestOption();
		requestOption.add("TNT");
		tntRequest.setRequest(reqType);
		
		RequestShipFromType shipFrom = new RequestShipFromType();
		RequestShipFromAddressType addressFrom = new RequestShipFromAddressType();
		addressFrom.setCity("Roswell");
		addressFrom.setCountryCode("US");
		addressFrom.setPostalCode("30076");
		addressFrom.setStateProvinceCode("GA");
		shipFrom.setAddress(addressFrom);
		tntRequest.setShipFrom(shipFrom);
		
		RequestShipToType shipTo = new RequestShipToType();
		RequestShipToAddressType addressTo = new RequestShipToAddressType();
		addressTo.setCity("Roswell");
		addressTo.setCountryCode("US");
		addressTo.setPostalCode("30076");
		addressTo.setStateProvinceCode("GA");
		shipTo.setAddress(addressTo);
		tntRequest.setShipTo(shipTo);
		
		PickupType pickup = new PickupType();
		pickup.setDate("20100715");
		tntRequest.setPickup(pickup);
		
		ShipmentWeightType shipmentWeight = new ShipmentWeightType();
		shipmentWeight.setWeight("10");		
		CodeDescriptionType unitOfMeasurement = new CodeDescriptionType();
		unitOfMeasurement.setCode("KGS");
		unitOfMeasurement.setDescription("Kilograms");
		shipmentWeight.setUnitOfMeasurement(unitOfMeasurement);
		tntRequest.setShipmentWeight(shipmentWeight);
		
		tntRequest.setTotalPackagesInShipment("1");
		InvoiceLineTotalType invoiceLineTotal = new InvoiceLineTotalType();
		invoiceLineTotal.setCurrencyCode("CAD");
		invoiceLineTotal.setMonetaryValue("10");
		tntRequest.setInvoiceLineTotal(invoiceLineTotal);
		tntRequest.setMaximumListSize("1");
		
		/** ************UPSSE***************************/
		UPSSecurity upsSecurity = new UPSSecurity();
		UsernameToken usernameToken = new UsernameToken();
		usernameToken.setUsername(username);
		usernameToken.setPassword(password);
		upsSecurity.setUsernameToken(usernameToken);
		ServiceAccessToken accessToken = new ServiceAccessToken();
		accessToken.setAccessLicenseNumber(accesskey);
		upsSecurity.setServiceAccessToken(accessToken);
		/** ************UPSSE******************************/

		TimeInTransitResponse tntResponse = tntPort.processTimeInTransit(tntRequest, upsSecurity);
		statusCode = tntResponse.getResponse().getResponseStatus().getCode();
		description = tntResponse.getResponse().getResponseStatus().getDescription();
		updateResultsToFile(statusCode, description);
		System.out.println("Transaction Status: "+ tntResponse.getResponse().getResponseStatus().getDescription());
		
		
    } catch (Exception e) {		
    	if(e instanceof TimeInTransitErrorMessage){
    		TimeInTransitErrorMessage err = (TimeInTransitErrorMessage)e;
    		Errors faultMessage = err.getFaultInfo();
    		description = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getDescription();
    		statusCode = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getCode();
    		updateResultsToFile(statusCode, description);
    		System.out.println("\nThe Error Response: Code=" + statusCode
    				+ " Decription=" + description);
    	}
    	else{
    		statusCode = e.getMessage();
    		description = e.toString();
    		updateResultsToFile(statusCode, description);
    	}

    }
		
	}
	/**
     * This method updates the XOLTResult.xml file with the received status and description
     * @param statusCode
     * @param description
     */
	 private static void updateResultsToFile(String statusCode, String description){
	    	BufferedWriter bw = null;
	    	try{    		
	    		
	    		File outFile = new File(props.getProperty(out_file_location));
	    		System.out.println("Output file deletion status: " + outFile.delete());
	    		outFile.createNewFile();
	    		System.out.println("Output file location: " + outFile.getCanonicalPath());
	    		bw = new BufferedWriter(new FileWriter(outFile));
	    		StringBuffer strBuf = new StringBuffer();
	    		strBuf.append("<ExecutionAt>");
	    		strBuf.append(Calendar.getInstance().getTime());
	    		strBuf.append("</ExecutionAt>\n");
	    		strBuf.append("<ToolOrWebServiceName>");
	    		strBuf.append(props.getProperty(tool_or_webservice_name));
	    		strBuf.append("</ToolOrWebServiceName>\n");
	    		strBuf.append("\n");
	    		strBuf.append("<ResponseStatus>\n");
	    		strBuf.append("\t<Code>");
	    		strBuf.append(statusCode);
	    		strBuf.append("</Code>\n");
	    		strBuf.append("\t<Description>");
	    		strBuf.append(description);
	    		strBuf.append("</Description>\n");
	    		strBuf.append("</ResponseStatus>");
	    		bw.write(strBuf.toString());
	    		bw.close();    		    		
	    	}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					if (bw != null){
						bw.close();
						bw = null;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}			
			}		
	    }
}
