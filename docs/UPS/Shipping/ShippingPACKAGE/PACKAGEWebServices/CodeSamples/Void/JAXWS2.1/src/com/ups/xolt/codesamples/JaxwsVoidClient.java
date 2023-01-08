/* 
 ** 
 ** Filename: JaxwsVoidClient.java  
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
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import com.ups.wsdl.xoltws.voidws.v1_1.CodeType;
import com.ups.wsdl.xoltws.voidws.v1_1.ErrorDetailType;
import com.ups.wsdl.xoltws.voidws.v1_1.Errors;
import com.ups.wsdl.xoltws.voidws.v1_1.RequestType;
import com.ups.wsdl.xoltws.voidws.v1_1.UPSSecurity;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidErrorMessage;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidPortType;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidService;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidShipmentRequest;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidShipmentResponse;
import com.ups.wsdl.xoltws.voidws.v1_1.UPSSecurity.ServiceAccessToken;
import com.ups.wsdl.xoltws.voidws.v1_1.UPSSecurity.UsernameToken;
import com.ups.wsdl.xoltws.voidws.v1_1.VoidShipmentRequest.VoidShipment;

//import com.ups.wsdl.xoltws.

public class JaxwsVoidClient {
	private static String url;
	private static String accessKey;
	private static String userName;
	private static String password;
	private static String buildPropertiesPath="./build.properties";
	private static String out_file_location="out_file_location";
	private static String tool_or_webservice_name="tool_or_webservice_name";
    private static String statusCode = null;
	private static String description = null;

	private static void loadProperties(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(buildPropertiesPath));
			
		} catch (IOException e) {
			statusCode = e.getMessage();
			description = e.toString();
			updateResultsToFile(statusCode, description);
			e.printStackTrace();
		}
		
		url=properties.getProperty("url");
		accessKey=properties.getProperty("accesskey");
		userName=properties.getProperty("username");
		password=properties.getProperty("password");
		out_file_location=properties.getProperty("out_file_location");
		tool_or_webservice_name=properties.getProperty("tool_or_webservice_name");
	}

	
	public static void main(String[] arguments) throws Exception {
		try {
			loadProperties();
			VoidService voidService = new VoidService();
			VoidPortType voidPort=voidService.getVoidPort();
			BindingProvider bp = (BindingProvider)voidPort;
	    	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
			VoidShipmentRequest voidRequest = new VoidShipmentRequest();
			RequestType request = new RequestType();			
			List requestOption = request.getRequestOption();
			requestOption.add("1");
			
			VoidShipment voidShipment = new VoidShipment();
			voidShipment.setShipmentIdentificationNumber("1Z2220060290261413");
			voidRequest.setRequest(request);
			voidRequest.setVoidShipment(voidShipment);

			/** ************UPSSE************************** */
			UPSSecurity upsSecurity = new UPSSecurity();
			UsernameToken usernameToken = new UsernameToken();
			usernameToken.setUsername(userName);
			usernameToken.setPassword(password);
			upsSecurity.setUsernameToken(usernameToken);
			ServiceAccessToken accessToken = new ServiceAccessToken();
			accessToken.setAccessLicenseNumber(accessKey);
			upsSecurity.setServiceAccessToken(accessToken);
			
			/** ************UPSSE***************************** */

			VoidShipmentResponse voidResponse = voidPort.processVoid(voidRequest, upsSecurity);
			System.out.println("The transaction was a "+voidResponse.getResponse().getResponseStatus().getDescription());
			System.out.println("The shipment has been "+voidResponse.getSummaryResult().getStatus().getDescription());
			statusCode = voidResponse.getResponse().getResponseStatus().getCode();
			description = voidResponse.getResponse().getResponseStatus().getDescription();
			updateResultsToFile(statusCode, description);

		}catch (VoidErrorMessage avE) {
			  Errors errs= avE.getFaultInfo();
			  List errDetailList = errs.getErrorDetail();
			  ErrorDetailType aError = (ErrorDetailType) errDetailList.get(0);
			  CodeType primaryError = aError.getPrimaryErrorCode();
			  description = primaryError.getDescription();   
			  statusCode = primaryError.getCode();
			  updateResultsToFile(statusCode, description);
			  System.out.println("\nThe Error Response: Code=" +statusCode + " Decription=" + description);
		}catch (Exception e) {
			description=e.getMessage();
			statusCode=e.toString();
			updateResultsToFile(statusCode, description);
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
	    		
	    		File outFile = new File(out_file_location);
	    		System.out.println("Output file deletion status: " + outFile.delete());
	    		outFile.createNewFile();
	    		System.out.println("Output file location: " + outFile.getCanonicalPath());
	    		bw = new BufferedWriter(new FileWriter(outFile));
	    		StringBuffer strBuf = new StringBuffer();
	    		strBuf.append("<ExecutionAt>");
	    		strBuf.append(Calendar.getInstance().getTime());
	    		strBuf.append("</ExecutionAt>\n");
	    		strBuf.append("<ToolOrWebServiceName>");
	    		strBuf.append(tool_or_webservice_name);
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
