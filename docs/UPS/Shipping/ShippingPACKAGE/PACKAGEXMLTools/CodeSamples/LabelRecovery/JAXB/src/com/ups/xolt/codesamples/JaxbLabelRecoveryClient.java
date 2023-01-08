/* 
 ** 
 ** Filename: JaxbLabelRecoveryClient.java 
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ups.xolt.codesamples.accessrequest.jaxb.AccessRequest;
import com.ups.xolt.codesamples.request.jaxb.CodeType;
import com.ups.xolt.codesamples.request.jaxb.LabelDeliveryType;
import com.ups.xolt.codesamples.request.jaxb.LabelLinkIndicator;
import com.ups.xolt.codesamples.request.jaxb.LabelRecoveryRequest;
import com.ups.xolt.codesamples.request.jaxb.LabelSpecificationType;
import com.ups.xolt.codesamples.request.jaxb.Request;
import com.ups.xolt.codesamples.response.jaxb.LabelRecoveryResponse;
import com.ups.xolt.codesamples.response.jaxb.LabelResultsType;
import com.ups.xolt.codesamples.response.jaxb.TrackingCandidateType;

public class JaxbLabelRecoveryClient {
	
	private static final String LICENSE_NUMBER = "accesskey";
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static final String ENDPOINT_URL="url";
	private static final String OUT_FILE_LOCATION = "out_file_location";
    private static Properties props = null;
	private static String description = null;
    static {
    	props = new Properties();
    	try{
    		props.load(new FileInputStream("./build.properties"));
    	}catch (Exception e) {
			description = e.toString();
			updateResultsToFile(description);
    		e.printStackTrace();
		}	
    }
    
    public static void main( String[] args ) {    
		String description = null;
		StringWriter strWriter = null;
        try {	    
        	
        	//Create JAXBContext and marshaller for AccessRequest object        			
        	JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage().getName() );	            
			Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory accessRequestObjectFactory = new com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory();
			AccessRequest accessRequest = accessRequestObjectFactory.createAccessRequest();
			populateAccessRequest(accessRequest);
			 
			//Create JAXBContext and marshaller for RatingServiceSelectionRequest object
			JAXBContext labelRecoveryRequestJAXBC = JAXBContext.newInstance(LabelRecoveryRequest.class.getPackage().getName() );
			Marshaller labelRecoveryRequestMarshaller = labelRecoveryRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.request.jaxb.ObjectFactory requestObjectFactory = new com.ups.xolt.codesamples.request.jaxb.ObjectFactory();
			LabelRecoveryRequest labelRecoveryRequest = requestObjectFactory.createLabelRecoveryRequest();
			populateLabelRecoveryRequest(labelRecoveryRequest);
			
			
			strWriter = new StringWriter();       		       
			accessRequestMarshaller.marshal(accessRequest, strWriter);
			labelRecoveryRequestMarshaller.marshal(labelRecoveryRequest, strWriter);
			strWriter.flush();
			strWriter.close();
			System.out.println("Request: " + strWriter.getBuffer().toString());
			
			String strResults =contactService(strWriter.getBuffer().toString());

			//Parse response object
			JAXBContext labelRecoveryJAXBC = JAXBContext.newInstance(LabelRecoveryResponse.class.getPackage().getName());
			Unmarshaller labelRecoveryUnmarshaller = labelRecoveryJAXBC.createUnmarshaller();
			ByteArrayInputStream input = new ByteArrayInputStream(strResults.getBytes());
			Object objResponse = labelRecoveryUnmarshaller.unmarshal(input);
			LabelRecoveryResponse labelRecoveryResponse = (LabelRecoveryResponse)objResponse;
			System.out.println("Response Status code: " + labelRecoveryResponse.getResponse().getResponseStatusCode());
			System.out.println("Response Status Description: " + labelRecoveryResponse.getResponse().getResponseStatusDescription());
			
			List<Object> labelResultList =  labelRecoveryResponse.getLabelResultsOrTrackingCandidate();
			if(labelResultList != null && labelResultList.size() > 0){
				int cnt = 0;
				while(cnt < labelResultList.size()){
					Object objLabelResultsOrTrackingCandidate = labelResultList.get(cnt);
					if(objLabelResultsOrTrackingCandidate instanceof LabelResultsType){
						LabelResultsType label = (LabelResultsType)objLabelResultsOrTrackingCandidate;
						System.out.println("Tracking Number: " + label.getTrackingNumber());
						System.out.println("Graphic Image: " + label.getLabelImage().getGraphicImage());
					}
					cnt++;
				}
			}
			
			
			
			updateResultsToFile(strResults);		   
			
        } catch (Exception e) {
        	description=e.toString();
			updateResultsToFile(description);
			e.printStackTrace();
		} finally{
			try{
				if(strWriter != null){
					strWriter.close();
					strWriter = null;
				}
			}catch (Exception e) {
				description=e.toString();
				updateResultsToFile(description);
				e.printStackTrace();
			}
		}
    }  
    
	private static String contactService(String xmlInputString) throws Exception{		
		String outputStr = null;
		OutputStream outputStream = null;
		HttpURLConnection connection=null;
		
		try {

			URL url = new URL(props.getProperty(ENDPOINT_URL));
			
			connection = (HttpURLConnection) url.openConnection();
			System.out.println("Client established connection with " + url.toString());
			// Setup HTTP POST parameters
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(1000);
			outputStream = connection.getOutputStream();		
			outputStream.write(xmlInputString.getBytes());
			outputStream.flush();
			outputStream.close();
			System.out.println("Http status = " + connection.getResponseCode() + " " + connection.getResponseMessage());
			
			outputStr = readURLConnection(connection);	
			System.out.println(outputStr);
		} catch (Exception e) {
			System.out.println("Error sending data to server");
			throw e;
		} finally {						
			if(outputStream != null){
				outputStream.close();
				outputStream = null;
				connection.disconnect();
				connection=null;
			}
		}		
		return outputStr;
	}
	
	/**
	 * This method read all of the data from a URL connection to a String
	 */

	public static String readURLConnection(URLConnection uc) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			int letter = 0;			
			while ((letter = reader.read()) != -1){
				buffer.append((char) letter);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Could not read from URL: " + e.toString());
			throw e;
		} finally {
			if(reader != null){
				reader.close();
				reader = null;
			}
		}
		return buffer.toString();
	}

    /**
     * Populates the access request object.
     * @param accessRequest
     */
    private static void populateAccessRequest(AccessRequest accessRequest){
    	accessRequest.setAccessLicenseNumber(props.getProperty(LICENSE_NUMBER));
    	accessRequest.setUserId(props.getProperty(USER_NAME));
    	accessRequest.setPassword(props.getProperty(PASSWORD));
    }
   
    /**
     * This method updates the XOLTResult.xml file with the received status and description
     * @param response
     */
    private static void updateResultsToFile(String response){
    	BufferedWriter bw = null;
    	try{    		
    		File outFile = new File(props.getProperty(OUT_FILE_LOCATION));
    		System.out.println("Output file deletion status: " + outFile.delete());
    		outFile.createNewFile();
    		System.out.println("Output file location: " + outFile.getCanonicalPath());
    		bw = new BufferedWriter(new FileWriter(outFile));
    		StringBuffer strBuf = new StringBuffer();
    		strBuf.append(response);
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
    
    private static void populateLabelRecoveryRequest(LabelRecoveryRequest labelRecoveryRequest) {
    	Request request = new Request();
    	request.setRequestAction("LabelRecovery");

    	LabelSpecificationType labelSpecification = new LabelSpecificationType();
    	CodeType codeType = new CodeType();
    	codeType.setCode("GIF");
    	labelSpecification.setLabelImageFormat(codeType);
    	labelSpecification.setHTTPUserAgent("");

    	LabelDeliveryType labelDeliveryType = new LabelDeliveryType();
    	LabelLinkIndicator labelLinkIndicator = new LabelLinkIndicator();
    	labelDeliveryType.setLabelLinkIndicator(labelLinkIndicator);

    	labelRecoveryRequest.setLabelDelivery(labelDeliveryType);
    	labelRecoveryRequest.setLabelSpecification(labelSpecification);
    	labelRecoveryRequest.setTrackingNumber("123456789");
    	labelRecoveryRequest.setRequest(request);

    }
}
