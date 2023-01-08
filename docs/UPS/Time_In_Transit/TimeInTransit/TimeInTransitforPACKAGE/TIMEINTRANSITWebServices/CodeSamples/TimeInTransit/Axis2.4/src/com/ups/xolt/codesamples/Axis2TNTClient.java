/* 
 ** 
 ** Filename: Axis2TNTClient.java 
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
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;


import com.ups.www.wsdl.xoltws.tnt.v1_0.TimeInTransitErrorMessage;
import com.ups.www.wsdl.xoltws.tnt.v1_0.TimeInTransitServiceStub;

public class Axis2TNTClient {
	
	private static String url;
	private static String accesskey;
	private static String username;
	private static String password;
	private static String out_file_location = "out_file_location";
	private static String tool_or_webservice_name = "tool_or_webservice_name";
	static Properties props = null;
	
	static{
		
        try{
        	props = new Properties();
        	props.load(new FileInputStream("./build.properties"));
	  		url = props.getProperty("url");
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
			
			TimeInTransitServiceStub tntServiceStub = new TimeInTransitServiceStub(url);
			TimeInTransitServiceStub.TimeInTransitRequest tntRequest = new TimeInTransitServiceStub.TimeInTransitRequest();			
			TimeInTransitServiceStub.RequestType request = new TimeInTransitServiceStub.RequestType();
			String[] requestOption = { "TNT" };
			request.setRequestOption(requestOption);
			tntRequest.setRequest(request);
			
			TimeInTransitServiceStub.RequestShipFromType shipFrom = new TimeInTransitServiceStub.RequestShipFromType();
			TimeInTransitServiceStub.RequestShipFromAddressType addressFrom = new TimeInTransitServiceStub.RequestShipFromAddressType();
			addressFrom.setCity("Roswell");
			addressFrom.setCountryCode("US");
			addressFrom.setPostalCode("30076");
			addressFrom.setStateProvinceCode("GA");
			shipFrom.setAddress(addressFrom);
			tntRequest.setShipFrom(shipFrom);
			
			TimeInTransitServiceStub.RequestShipToType shipTo = new TimeInTransitServiceStub.RequestShipToType();
			TimeInTransitServiceStub.RequestShipToAddressType addressTo = new TimeInTransitServiceStub.RequestShipToAddressType();
			addressTo.setCity("Roswell");
			addressTo.setCountryCode("US");
			addressTo.setPostalCode("30076");
			addressTo.setStateProvinceCode("GA");
			shipTo.setAddress(addressTo);
			tntRequest.setShipTo(shipTo);
			
			TimeInTransitServiceStub.PickupType pickup = new TimeInTransitServiceStub.PickupType();
			pickup.setDate("20100715");
			tntRequest.setPickup(pickup);
			
			TimeInTransitServiceStub.ShipmentWeightType shipmentWeight = new TimeInTransitServiceStub.ShipmentWeightType();
			shipmentWeight.setWeight("10");
			TimeInTransitServiceStub.CodeDescriptionTypeE unitOfMeasurement = new TimeInTransitServiceStub.CodeDescriptionTypeE();
			unitOfMeasurement.setCode("KGS");
			unitOfMeasurement.setDescription("Kilograms");
			shipmentWeight.setUnitOfMeasurement(unitOfMeasurement);
			tntRequest.setShipmentWeight(shipmentWeight);
			
			tntRequest.setTotalPackagesInShipment("1");
			TimeInTransitServiceStub.InvoiceLineTotalType invoiceLineTotal = new TimeInTransitServiceStub.InvoiceLineTotalType();
			invoiceLineTotal.setCurrencyCode("CAD");
			invoiceLineTotal.setMonetaryValue("10");
			tntRequest.setInvoiceLineTotal(invoiceLineTotal);
			tntRequest.setMaximumListSize("1");
			
			/** ************UPSSE***************************/
			TimeInTransitServiceStub.UPSSecurity upss = new TimeInTransitServiceStub.UPSSecurity();
			TimeInTransitServiceStub.UsernameToken_type0 upsUsrToken = new TimeInTransitServiceStub.UsernameToken_type0();
			upsUsrToken.setPassword(password);
			upsUsrToken.setUsername(username);
			TimeInTransitServiceStub.ServiceAccessToken_type0 token = new TimeInTransitServiceStub.ServiceAccessToken_type0();
			token.setAccessLicenseNumber(accesskey);
			upss.setUsernameToken(upsUsrToken);
			upss.setServiceAccessToken(token);
			/** ************UPSSE******************************/
			
			TimeInTransitServiceStub.TimeInTransitResponse tntResponse = tntServiceStub.ProcessTimeInTransit(tntRequest, upss);
			statusCode = tntResponse.getResponse().getResponseStatus().getCode();
			description = tntResponse.getResponse().getResponseStatus().getDescription();
			updateResultsToFile(statusCode, description);
			System.out.println("Transaction Status: "
					+ tntResponse.getResponse().getResponseStatus()
							.getDescription());
			System.out.println("Response"+tntServiceStub.ProcessTimeInTransit(tntRequest, upss));
		} catch (Exception e) {
			if (e instanceof TimeInTransitErrorMessage){
				TimeInTransitErrorMessage err = (TimeInTransitErrorMessage)e;
				statusCode = err.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode();
				description = err.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription();
				updateResultsToFile(statusCode, description);
				 
			 }else{
				 statusCode = e.getMessage();
				 description = e.toString();
				 updateResultsToFile(statusCode, description);
			 }
			e.printStackTrace();
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
