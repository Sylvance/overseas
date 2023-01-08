/* 
 ** 
 ** Filename: JAXWSLBRecoveryClient.java 
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
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import com.ups.wsdl.xoltws.lbrecovery.v1.LabelRecoveryErrorMessage;
import com.ups.wsdl.xoltws.lbrecovery.v1.PortType;
import com.ups.wsdl.xoltws.lbrecovery.v1.LBRecovery;
import com.ups.xmlschema.xoltws.common.v1.RequestType;
import com.ups.xmlschema.xoltws.lbrecovery.v1.LabelRecoveryRequest;
import com.ups.xmlschema.xoltws.lbrecovery.v1.LabelRecoveryResponse;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity;
public class JAXWSLBRecoveryClient {   
    static LBRecovery service;
    
    private static final String LICENSE_NUMBER = "accesskey";
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static final String OUT_FILE_LOCATION = "out_file_location";
	private static final String TOOL_OR_WEB_SERVICE_NAME = "tool_or_webservice_name";
	private static final String ENDPOINT_URL = "url";
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	try {
        	service = new LBRecovery();
        	PortType lbRecoveryPortType = service.getPort();
        	
        	BindingProvider bp = (BindingProvider)lbRecoveryPortType;
         	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, props.getProperty(ENDPOINT_URL));
            
         	LabelRecoveryResponse lBRecoveryResponse = lbRecoveryPortType.processLabelRecovery(populateLabelRecoveryRequest(), populateUPSSecurity());            
            statusCode = lBRecoveryResponse.getResponse().getResponseStatus().getCode();
            description = lBRecoveryResponse.getResponse().getResponseStatus().getDescription();
            updateResultsToFile(statusCode, description);
        } catch(Exception e) {
        	if(e  instanceof LabelRecoveryErrorMessage){
        		LabelRecoveryErrorMessage trkErrorMessage = (LabelRecoveryErrorMessage)e;
        		statusCode = trkErrorMessage.getFaultInfo().getErrorDetail().get(0).getPrimaryErrorCode().getCode();
        		description = trkErrorMessage.getFaultInfo().getErrorDetail().get(0).getPrimaryErrorCode().getDescription();
        		updateResultsToFile(statusCode, description);
        	} else {
        		statusCode = e.getMessage();
        		description = e.toString();
        		updateResultsToFile(statusCode, description);
        	}    		
            e.printStackTrace();
        }
    }

    private static LabelRecoveryRequest populateLabelRecoveryRequest(){
    	
    	LabelRecoveryRequest lBRecoveruRequest = new LabelRecoveryRequest();
		RequestType request = new RequestType();
		
		lBRecoveruRequest.setRequest(request);
		lBRecoveruRequest.setTrackingNumber("1ZAA75228494956094");
    	return lBRecoveruRequest;
    }
    
    private static UPSSecurity populateUPSSecurity(){
    	UPSSecurity upss = new UPSSecurity();
		UPSSecurity.ServiceAccessToken upsSvcToken = new UPSSecurity.ServiceAccessToken();
		upsSvcToken.setAccessLicenseNumber(props.getProperty(LICENSE_NUMBER));
		upss.setServiceAccessToken(upsSvcToken);
		UPSSecurity.UsernameToken upsSecUsrnameToken = new UPSSecurity.UsernameToken();
		upsSecUsrnameToken.setUsername(props.getProperty(USER_NAME));
		upsSecUsrnameToken.setPassword(props.getProperty(PASSWORD));
		upss.setUsernameToken(upsSecUsrnameToken);
		
		return upss;
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
