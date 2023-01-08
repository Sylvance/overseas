/* 
 ** 
 ** Filename: JAXBTNTClient.java 
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.ups.xolt.codesamples.accessrequest.jaxb.AccessRequest;
import com.ups.xolt.codesamples.request.jaxb.AddressArtifactFormatType;
import com.ups.xolt.codesamples.request.jaxb.CodeType;
import com.ups.xolt.codesamples.request.jaxb.MonetaryType;
import com.ups.xolt.codesamples.request.jaxb.TimeInTransitRequest;
import com.ups.xolt.codesamples.request.jaxb.TransitFromType;
import com.ups.xolt.codesamples.request.jaxb.TransitToAddressArtifactFormatType;
import com.ups.xolt.codesamples.request.jaxb.TransitToType;
import com.ups.xolt.codesamples.request.jaxb.WeightType;
import com.ups.xolt.codesamples.request.jaxb.Request;

public class JAXBTNTClient {
	
	private static final String accesskey = "accesskey";
	private static final String username = "username";
	private static final String password = "password";
	private static final String endpoint_url="url";
	private static String out_file_location = "out_file_location";
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
    	
    	String statusCode = null;
		String description = null;
		StringWriter strWriter = null;
        try {	    
        	
        	//Create JAXBContext and marshaller for AccessRequest object        			
        	JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage().getName() );	            
			Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory accessRequestObjectFactory = new com.ups.xolt.codesamples.accessrequest.jaxb.ObjectFactory();
			AccessRequest  accessRequest = accessRequestObjectFactory.createAccessRequest();
			populateAccessRequest(accessRequest);
			 
			//Create JAXBContext and marshaller for TimeInTransitRequest object
			JAXBContext tntRequestJAXBC = JAXBContext.newInstance(TimeInTransitRequest.class.getPackage().getName() );	            
			Marshaller tntRequestMarshaller = tntRequestJAXBC.createMarshaller();
			com.ups.xolt.codesamples.request.jaxb.ObjectFactory requestObjectFactory = new com.ups.xolt.codesamples.request.jaxb.ObjectFactory();
			TimeInTransitRequest tntRequest = requestObjectFactory.createTimeInTransitRequest();
			populateTNTRequest(tntRequest);			
			//Get String out of access request and AddressValidationRequest objects.
			strWriter = new StringWriter();       		       
			accessRequestMarshaller.marshal(accessRequest, strWriter);
			tntRequestMarshaller.marshal(tntRequest, strWriter);
			strWriter.flush();
			strWriter.close();
			System.out.println("Request: " + strWriter.getBuffer().toString());
			
			String strResults =contactService(strWriter.getBuffer().toString());
			updateResultsToFile(strResults);		   
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

			URL url = new URL(props.getProperty(endpoint_url));
			
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
    	accessRequest.setAccessLicenseNumber(props.getProperty(accesskey));
    	accessRequest.setUserId(props.getProperty(username));
    	accessRequest.setPassword(props.getProperty(password));
    }
     /**
     * Populate TimeInTransitRequest object
     * @param tntRequest
     */
    private static void populateTNTRequest(TimeInTransitRequest tntRequest){   	
    	
    	Request request = new Request();
    	request.setRequestAction("TimeInTransit");
    	tntRequest.setRequest(request);
    	
		TransitFromType shipFrom = new TransitFromType();
		AddressArtifactFormatType addressFrom = new AddressArtifactFormatType();
		addressFrom.setCountryCode("IE");
		addressFrom.setPoliticalDivision2("Carrickmacross");
		shipFrom.setAddressArtifactFormat(addressFrom);
		tntRequest.setTransitFrom(shipFrom);
		
		TransitToType shipTo = new TransitToType();
		TransitToAddressArtifactFormatType addressTo = new TransitToAddressArtifactFormatType();
		addressTo.setCountryCode("SE");
		addressTo.setPoliticalDivision2("Stockholm");
		addressTo.setPostcodePrimaryLow("11187");
		shipTo.setAddressArtifactFormat(addressTo);
		tntRequest.setTransitTo(shipTo);
		
		tntRequest.setPickupDate("20100715");
		
		WeightType shipmentWeight = new WeightType();
		shipmentWeight.setWeight("10");
		CodeType unitOfMeasurement = new CodeType();
		unitOfMeasurement.setCode("KGS");
		unitOfMeasurement.setDescription("Kilograms");
		shipmentWeight.setUnitOfMeasurement(unitOfMeasurement);
		tntRequest.setShipmentWeight(shipmentWeight);
		
		tntRequest.setTotalPackagesInShipment("1");
		MonetaryType invoiceLineTotal = new MonetaryType();
		invoiceLineTotal.setCurrencyCode("EUR");
		invoiceLineTotal.setMonetaryValue("10");
		tntRequest.setInvoiceLineTotal(invoiceLineTotal);
		tntRequest.setMaximumListSize("10");
    }
    
    /**
     * This method updates the XOLTResult.xml file with the received status and description
     * @param statusCode
     * @param description
     */
    private static void updateResultsToFile(String response){
    	BufferedWriter bw = null;
    	try{    		
    		
    		File outFile = new File(props.getProperty(out_file_location));
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
}