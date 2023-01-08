/*
 **
 ** Filename: Axis2VoidClient.java
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

import com.ups.www.wsdl.xoltws.voidws.v1_1.VoidErrorMessage;
import com.ups.www.wsdl.xoltws.voidws.v1_1.VoidServiceStub;
import com.ups.www.wsdl.xoltws.voidws.v1_1.VoidServiceStub.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Calendar;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;



public class Axis2VoidClient {
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

	public static void main(String[] args) {
		try {
			loadProperties();
			VoidServiceStub voidServiceStub = new VoidServiceStub(url);
			VoidShipmentRequest voidRequest = new VoidShipmentRequest();
			RequestType request = new RequestType();
			String[] requestOption = { "1" };
			request.setRequestOption(requestOption);			
			voidRequest.setRequest(request);
			VoidShipment_type0 voidShipment = new VoidShipment_type0();
			voidShipment.setShipmentIdentificationNumber("1Z2220060290261413");			
			voidRequest.setVoidShipment(voidShipment);
			

			/** ************UPSSE************************** */
			UPSSecurity upss = new UPSSecurity();
			ServiceAccessToken_type0 upsSvcToken = new ServiceAccessToken_type0();
			upsSvcToken.setAccessLicenseNumber(accessKey);
			upss.setServiceAccessToken(upsSvcToken);
			UsernameToken_type0 upsSecUsrnameToken = new UsernameToken_type0();
			upsSecUsrnameToken.setUsername(userName);
			upsSecUsrnameToken.setPassword(password);
			upss.setUsernameToken(upsSecUsrnameToken);
			/** ************UPSSE***************************** */

			VoidShipmentResponse voidResponse = voidServiceStub.ProcessVoid(voidRequest, upss);
			System.out.println("The transaction was a "+voidResponse.getResponse().getResponseStatus().getDescription());
			System.out.println("The shipment has been "+voidResponse.getSummaryResult().getStatus().getDescription());
			statusCode = voidResponse.getResponse().getResponseStatus().getCode();
            description = voidResponse.getResponse().getResponseStatus().getDescription();
			updateResultsToFile(statusCode, description);


		} catch (Exception e) {
			description=e.getMessage();
			statusCode=e.toString();
			if (e instanceof VoidErrorMessage){
				VoidErrorMessage voidErr = (VoidErrorMessage)e;
				System.out.print("Receieved Error "+voidErr.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode()+" ");
				System.out.println(voidErr.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription());
				description=voidErr.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription();
				statusCode=voidErr.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode();
			}
			 else{
				 e.printStackTrace();
			 }
			updateResultsToFile(statusCode, description);
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



