/* 
 ** 
 ** Filename: JAXBLocatorClient.java 
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
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ups.xolt.codesamples.accessrequest.jaxb.AccessRequest;
import com.ups.xolt.codesamples.request.jaxb.AddressKeyFormatType;
import com.ups.xolt.codesamples.request.jaxb.CodeType;
import com.ups.xolt.codesamples.request.jaxb.LocationSearchCriteriaType;
import com.ups.xolt.codesamples.request.jaxb.LocatorRequest;
import com.ups.xolt.codesamples.request.jaxb.OriginAddressType;
import com.ups.xolt.codesamples.request.jaxb.Request;
import com.ups.xolt.codesamples.request.jaxb.ServiceSearchType;
import com.ups.xolt.codesamples.request.jaxb.TransactionReference;
import com.ups.xolt.codesamples.request.jaxb.TranslateType;
import com.ups.xolt.codesamples.request.jaxb.UnitOfMeasurementType;
import com.ups.xolt.codesamples.response.jaxb.DropLocationType;
import com.ups.xolt.codesamples.response.jaxb.LocatorResponse;
import com.ups.xolt.codesamples.response.jaxb.SearchResultsType;

public class JAXBLocatorClient {
	private static final String LICENSE_NUMBER = "LICENSE_NUMBER";
	private static final String USER_NAME = "USER_NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String ENDPOINT_URL="ENDPOINT_URL";
	private static final String OUT_FILE_LOCATION ="OUT_FILE_LOCATION";
    static Properties props = null;

    static{
    	props = new Properties();
    	try{
    		props.load(new FileInputStream("./build.properties"));
    	}catch (Exception e) {
			e.printStackTrace();
		}    	
    }
	
    public static void main( String[] args ) {    
    	
		StringWriter strWriter = null;
		String statusCode = null;
		String description = null;
        try {	    
        	
        	//Create JAXBContext and marshaller for AccessRequest object        			
        	JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage().getName() );	            
			Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory accessRequestObjectFactory = new com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory();
			AccessRequest accessRequest = accessRequestObjectFactory.createAccessRequest();
			populateAccessRequest(accessRequest);
			 
			//Create JAXBContext and marshaller for LocatorRequest object
			JAXBContext locatorRequestJAXBC = JAXBContext.newInstance(LocatorRequest.class.getPackage().getName() );	            
			Marshaller locatorRequestMarshaller = locatorRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.request.jaxb.ObjectFactory requestObjectFactory = new com.ups.xolt.codesamples.request.jaxb.ObjectFactory();
			LocatorRequest locatorRequest = requestObjectFactory.createLocatorRequest();
			populateLocatorServiceSelectionRequest(locatorRequest);
			
			//Get String out of access request and Locator request objects.
			strWriter = new StringWriter();       		       
			accessRequestMarshaller.marshal(accessRequest, strWriter);
			locatorRequestMarshaller.marshal(locatorRequest, strWriter);
			strWriter.flush();
			strWriter.close();
			System.out.println("Request: " + strWriter.getBuffer().toString());
			String strResults =contactService(strWriter.getBuffer().toString());
			
			//Parse response object
			JAXBContext locatorResponseJAXBC = JAXBContext.newInstance(LocatorResponse.class.getPackage().getName());
			Unmarshaller locatorResponseUnmarshaller = locatorResponseJAXBC.createUnmarshaller();
			ByteArrayInputStream input = new ByteArrayInputStream(strResults.getBytes());
			Object objResponse = locatorResponseUnmarshaller.unmarshal(input);
			LocatorResponse locatorResponse = (LocatorResponse)objResponse;
			//print out response status description
			statusCode = locatorResponse.getResponse().getResponseStatusCode();
			description = locatorResponse.getResponse().getResponseStatusDescription();
			System.out.println("Response Status: " + locatorResponse.getResponse().getResponseStatusDescription());
			System.out.println("Latitude: " + locatorResponse.getGeocode().getLatitude());
			System.out.println("Longitude: " + locatorResponse.getGeocode().getLongitude());
			SearchResultsType searchResult = locatorResponse.getSearchResults();
			List<Object> candidateList = searchResult.getDisclaimerAndDropLocation();
			if(candidateList != null && candidateList.size() > 0){
				int cnt = 0;
				while(cnt < candidateList.size()){
					Object candidate = candidateList.get(cnt);
					if(candidate instanceof DropLocationType)
					{
						System.out.println("LocationID: " + ((DropLocationType)candidate).getLocationID());
						System.out.println("LatesetGroundDropOffTime: " + ((DropLocationType)candidate).getLatestGroundDropOffTime());
						System.out.println("ConsigneeName: " + ((DropLocationType)candidate).getAddressKeyFormat().getConsigneeName());
						System.out.println("Address Line: " + ((DropLocationType)candidate).getAddressKeyFormat().getAddressLine());
						System.out.println("Country Code: " + ((DropLocationType)candidate).getAddressKeyFormat().getCountryCode());
						System.out.println("Political Division2: " + ((DropLocationType)candidate).getAddressKeyFormat().getPoliticalDivision2());
						System.out.println("Political Division1: " + ((DropLocationType)candidate).getAddressKeyFormat().getPoliticalDivision1());
						System.out.println("PostCodePrimaryLow : " + ((DropLocationType)candidate).getAddressKeyFormat().getPostcodePrimaryLow());
						System.out.println("PostCodeExtendedLow: " + ((DropLocationType)candidate).getAddressKeyFormat().getPostcodeExtendedLow());
						System.out.println(" ");
					}
					cnt++;
				}
			}
			updateResultsToFile(statusCode,description);
			
		} catch (Exception e) {
			updateResultsToFile(e.toString());
			e.printStackTrace();
		} finally{
			try{
				if(strWriter != null){
					strWriter.close();
					strWriter = null;
				}
			}catch (Exception e) {
				updateResultsToFile(e.toString());
				e.printStackTrace();
			}
		}
	}  
    
	private static String contactService(String xmlInputString) throws Exception{		
		String outputStr = null;
		OutputStream outputStream = null;
		try {
			URL url = new URL(props.getProperty(ENDPOINT_URL));
	
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			System.out.println("Client established connection with " + url.toString());
			// Setup HTTP POST parameters
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			
			outputStream = connection.getOutputStream();		
			outputStream.write(xmlInputString.getBytes());
			outputStream.flush();
			outputStream.close();
			System.out.println("Http status = " + connection.getResponseCode() + " " + connection.getResponseMessage());
			outputStr = readURLConnection(connection);	
			
			connection.disconnect();
			System.out.println("outputStr:"+ outputStr);
		} catch (Exception e) {
			System.out.println("Error sending data to server");
			throw e;
		} finally {						
			if(outputStream != null){
				outputStream.close();
				outputStream = null;
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
     * Populate LocatorRequest object
     * @param LocatorRequest
     */
    private static void populateLocatorServiceSelectionRequest(LocatorRequest locatorRequest){   	
   
    	Request request = new Request();
    	request.setRequestAction("Locator");
    	request.setRequestOption("3");
    	TransactionReference trRef = new TransactionReference();    	
    	request.setTransactionReference(trRef);
     	locatorRequest.setRequest(request);
     	
     	OriginAddressType origianAddType = new OriginAddressType();
     	
     	AddressKeyFormatType addressType = new AddressKeyFormatType();
     	addressType.setAddressLine("1830 East Randall Drive");
     	addressType.setPoliticalDivision2("Tempe");
     	addressType.setPoliticalDivision1("AZ");
     	addressType.setPostcodeExtendedLow("85281");
     	addressType.setPostcodePrimaryLow("4510");
     	addressType.setCountryCode("US");
     	origianAddType.setAddressKeyFormat(addressType);
     	locatorRequest.setOriginAddress(origianAddType);
     	
     	TranslateType translate = new TranslateType();
     	translate.setLanguageCode("ENG");
     	locatorRequest.setTranslate(translate);
     	
     	UnitOfMeasurementType unit = new UnitOfMeasurementType();
     	unit.setCode("MI");
     	locatorRequest.setUnitOfMeasurement(unit);
     	
     	LocationSearchCriteriaType location = new LocationSearchCriteriaType();
     	ServiceSearchType search = new ServiceSearchType();
     	search.setTime("1600");   
     	
     	CodeType codeType = new CodeType();
    	codeType.setCode("001");    	
    	search.getServiceCode().add(codeType);
    	
    	location.setServiceSearch(search);    	
    	locatorRequest.setLocationSearchCriteria(location);
 }
    /**
     * This method updates the XOLTResult.xml file with the received exception
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
    		strBuf.append("\n");
    		strBuf.append("<ResponseStatus>\n");
    		strBuf.append("\t<Code>");
    		strBuf.append(statusCode);
    		strBuf.append("</Code>\n");
    		strBuf.append("\t<Description>");
    		strBuf.append(description);
    		strBuf.append("</Description>\n");
    		strBuf.append("</ResponseStatus>");    		
    		//strBuf.append(response);
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