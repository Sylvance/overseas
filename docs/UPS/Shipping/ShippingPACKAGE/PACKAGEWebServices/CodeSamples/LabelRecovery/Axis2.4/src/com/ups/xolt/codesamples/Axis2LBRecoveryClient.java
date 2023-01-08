/* 
 ** 
 ** Filename: Axis2LBRecoveryClient.java 
  ** Authors: United Parcel Service of America
 ** 
 ** The use, disclosure, reproduction, modification, transfer, or transmittal 
 ** of this work for any purpose in any form or by any means without the 
 ** written permission of United Parcel Service is strictly prohibited. 
 ** 
 ** Confidential, Unpublished Property of United Parcel Service. 
 ** Use and Distribution Limited Solely to Authorized Personnel. 
 ** 
 ** Copyright 2013 United Parcel Service of America, Inc.  All Rights Reserved. 
 ** 
 */
package com.ups.xolt.codesamples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Properties;

import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LabelRecoveryErrorMessage;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.LabelRecoveryRequest;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.LabelRecoveryResponse;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.UPSSecurity;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.ServiceAccessToken_type0;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.UsernameToken_type0;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.LabelRecoveryRequestChoice_type0;
import com.ups.www.wsdl.xoltws.lbrecovery.v1_1.LBRecoveryStub.RequestType;


public class Axis2LBRecoveryClient {

	private static final String LICENSE_NUMBER = "accesskey";
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static final String ENDPOINT_URL="url";
	private static final String OUT_FILE_LOCATION = "out_file_location";
	private static final String TOOL_OR_WEB_SERVICE_NAME = "tool_or_webservice_name";
    private static Properties props = null;
    private static String statusCode = null;
	private static String description = null;
    static {
    	props = new Properties();
    	try{
    		props.load(new FileInputStream("./build.properties"));
    	}catch (Exception e) {
			statusCode = e.getMessage();
			description = e.toString();
			updateResultsToFile(statusCode, description);
    		e.printStackTrace();
		}	
    }
    
	public static void main(String[] arguments) throws Exception {
		try {
			LBRecoveryStub lBRecoveryStub = new LBRecoveryStub(props.getProperty(ENDPOINT_URL));
			
			UPSSecurity upss = new UPSSecurity();
			ServiceAccessToken_type0 upsSvcToken = new ServiceAccessToken_type0();
			upsSvcToken.setAccessLicenseNumber(props.getProperty(LICENSE_NUMBER));
			upss.setServiceAccessToken(upsSvcToken);
			UsernameToken_type0 upsSecUsrnameToken = new UsernameToken_type0();
			upsSecUsrnameToken.setUsername(props.getProperty(USER_NAME));
			upsSecUsrnameToken.setPassword(props.getProperty(PASSWORD));
			upss.setUsernameToken(upsSecUsrnameToken);
			
			LabelRecoveryResponse tr = lBRecoveryStub.processLabelRecovery(populateLBRecoveryRequest(), upss);			
			statusCode = tr.getResponse().getResponseStatus().getCode();
			description = tr.getResponse().getResponseStatus().getDescription();
			updateResultsToFile(statusCode, description);
		} catch (Exception e) {
			if(e instanceof LabelRecoveryErrorMessage){				
				LabelRecoveryErrorMessage tErrorMessage = (LabelRecoveryErrorMessage)e;
				statusCode = tErrorMessage.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode();
				description = tErrorMessage.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription();
				updateResultsToFile(statusCode, description);
			} else {
    			statusCode = e.getMessage();
    			description = e.toString();
    			updateResultsToFile(statusCode, description);	
        	}	
			e.printStackTrace();
		}
	}
	
	private static LabelRecoveryRequest populateLBRecoveryRequest(){
	
		LabelRecoveryRequest lbRequest = new LabelRecoveryRequest();
		RequestType request = new RequestType();
		lbRequest.setRequest(request);
		LabelRecoveryRequestChoice_type0 lrRequestChoise = new LabelRecoveryRequestChoice_type0();
		lrRequestChoise.setTrackingNumber("1ZAA75228494956094");
		lbRequest.setLabelRecoveryRequestChoice_type0(lrRequestChoise);
		return lbRequest;
	}
	
	
	/**
     * This method updates the XOLTResult.xml file with the received status and description
     * @param statusCode
     * @param description
     */
	   private static void updateResultsToFile(String statusCode, String description){
	    	BufferedWriter bw = null;
	    	try{    		
	    		File outFile = new File(props.getProperty(OUT_FILE_LOCATION));
	    		System.out.println("Output file deletion status: " + outFile.delete());
	    		outFile.createNewFile();
	    		System.out.println("Output file location: " + outFile.getCanonicalPath());
	    		bw = new BufferedWriter(new FileWriter(outFile));
	    		StringBuffer strBuf = new StringBuffer();
	    		strBuf.append("<ExecutionAt>");
	    		strBuf.append(Calendar.getInstance().getTime());
	    		strBuf.append("</ExecutionAt>\n");
	    		strBuf.append("<ToolOrWebServiceName>");
	    		strBuf.append(props.getProperty(TOOL_OR_WEB_SERVICE_NAME));
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
