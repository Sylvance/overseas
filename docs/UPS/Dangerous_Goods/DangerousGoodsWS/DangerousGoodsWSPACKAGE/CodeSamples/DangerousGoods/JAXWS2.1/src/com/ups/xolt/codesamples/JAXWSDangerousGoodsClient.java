package com.ups.xolt.codesamples;


/* 
 ** 
 ** Filename: JAXWSDangerousGoodsClient.java 
 ** Authors: United Parcel Service of America
 ** 
 ** The use, disclosure, reproduction, modification, transfer, or transmittal 
 ** of this work for any purpose in any form or by any means without the 
 ** written permission of United Parcel Service is strictly prohibited. 
 ** 
 ** Confidential, Unpublished Property of United Parcel Service. 
 ** Use and Distribution Limited Solely to Authorized Personnel. 
 ** 
 ** Copyright 2017 United Parcel Service of America, Inc.  All Rights Reserved. 
 ** 
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import com.ups.wsdl.xoltws.dangerousgoodsutility.v1.AcceptanceAuditPreCheckErrorMessage;
import com.ups.wsdl.xoltws.dangerousgoodsutility.v1.ChemicalReferenceDataErrorMessage;
import com.ups.wsdl.xoltws.dangerousgoodsutility.v1.DangerousGoodsUtility;
import com.ups.wsdl.xoltws.dangerousgoodsutility.v1.DangerousGoodsUtilityPortType;
import com.ups.xmlschema.xoltws.common.v1.RequestType;
import com.ups.xmlschema.xoltws.common.v1.TransactionReferenceType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.AcceptanceAuditPreCheckRequest;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.AcceptanceAuditPreCheckResponse;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.AddressType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalDataType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalDetailType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalRecordResultsType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalRecordType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalReferenceDataRequest;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ChemicalReferenceDataResponse;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.PackageResultsType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.PackageType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.PackageWeightType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.PackageWeightUOMType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ServiceType;
import com.ups.xmlschema.xoltws.dangerousgoodsutility.v1.ShipmentType;
import com.ups.xmlschema.xoltws.error.v1.Errors;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity.ServiceAccessToken;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity.UsernameToken;

public class JAXWSDangerousGoodsClient {
	
	private static String url;
	private static String accessKey;
	private static String userName;
	private static String password;
	private static String buildPropertiesPath = "./build.properties";
	private static String out_file_location = "out_file_location";
	private static String tool_or_webservice_name = "tool_or_webservice_name";
	private static String statusCode = null;
	private static String description = null;

	
	public static void main(String[] arguments) throws Exception 
	{
		
		loadProperties();
	
		/*
		 * Set the boolean value true depending upon type of request {Either Acceptance Audit Pre-Check or Chemical Reference Data request}
		 */
		boolean isAcceptanceAuditPreCheckRequest = true;  //For Acceptance Audit Pre-Check request, set the value to true.
		boolean isChemicalReferenceDataRequest = false; //For Chemical Reference Data request, set the value to true.
		
		DangerousGoodsUtility dangerousGoodsUtilityService = new DangerousGoodsUtility();
		DangerousGoodsUtilityPortType dangerousGoodsUtilityPort = dangerousGoodsUtilityService.getDangerousGoodsUtilityPort();
		BindingProvider bp = (BindingProvider) dangerousGoodsUtilityPort;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		RequestType requestType = new RequestType();
		TransactionReferenceType transactionReference = new TransactionReferenceType();
		
		UPSSecurity upsSecurity = new UPSSecurity();
		UsernameToken usernameToken = new UsernameToken();
		usernameToken.setUsername(userName);
		usernameToken.setPassword(password);
		upsSecurity.setUsernameToken(usernameToken);
		ServiceAccessToken accessToken = new ServiceAccessToken();
		accessToken.setAccessLicenseNumber(accessKey);
		upsSecurity.setServiceAccessToken(accessToken);
		
		if(isChemicalReferenceDataRequest)
		{
			try 
			{
				transactionReference.setCustomerContext("JAX-WS Test Client Program for Chemical Reference Data Request.");
				requestType.setTransactionReference(transactionReference);
			  
				ChemicalReferenceDataRequest chemicalReferenceDataReq = new ChemicalReferenceDataRequest();
				chemicalReferenceDataReq.setShipperNumber("Set your Shipper Number here.");
				chemicalReferenceDataReq.setProperShippingName("3-METHYL-1-BUTENE");
				chemicalReferenceDataReq.setIDNumber("UN2561");
			  
				ChemicalReferenceDataResponse chemicalReferenceDataRes = dangerousGoodsUtilityPort.processChemicalReferenceData(chemicalReferenceDataReq, upsSecurity);

				System.out.println("Transaction Status: " + chemicalReferenceDataRes.getResponse().getResponseStatus().getDescription());
			  
				if((chemicalReferenceDataRes.getChemicalData() != null) && (chemicalReferenceDataRes.getChemicalData().size() > 0))
				{
					int chemicalDataSize = chemicalReferenceDataRes.getChemicalData().size();
					for(int i=0; i < chemicalDataSize; i++)
					{
						ChemicalDataType chemData = chemicalReferenceDataRes.getChemicalData().get(i);
						if(chemData != null)
						{
							if(chemData.getChemicalDetail() != null)
							{
								ChemicalDetailType chemDetailType = chemData.getChemicalDetail();
								if(!isNullOrEmpty(chemDetailType.getClassDivisionNumber()))
								{
									System.out.println("ClassDivisonNumber for ChemicalData  " + i + " is " + chemDetailType.getClassDivisionNumber());
								}
							  
								if(!isNullOrEmpty(chemDetailType.getIDNumber()))
								{
									System.out.println("IDNumber for ChemicalData  " + i + " is " + chemDetailType.getIDNumber());
								}
							  
								if(!isNullOrEmpty(chemDetailType.getHazardousMaterialsDescription()))
								{
									System.out.println("HazardousMaterialsDescription for ChemicalData  " + i + " is " + chemDetailType.getHazardousMaterialsDescription());
								}
							  
								if(!isNullOrEmpty(chemDetailType.getPackagingGroupType()))
								{
									System.out.println("PackagingGroupType for ChemicalData  " + i + " is " + chemDetailType.getPackagingGroupType());
								}
							  
								if(!isNullOrEmpty(chemDetailType.getSubRiskClass()))
								{
									System.out.println("SubRiskClass for ChemicalData  " + i + " is " + chemDetailType.getSubRiskClass());
								}
							 }
						}
					}
				}
				statusCode = chemicalReferenceDataRes.getResponse().getResponseStatus().getCode();
				description = chemicalReferenceDataRes.getResponse().getResponseStatus().getDescription();
				updateResultsToFile(statusCode, description);
			 }
			catch (ChemicalReferenceDataErrorMessage e) 
			{
				Errors faultMessage = e.getFaultInfo();
				description = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getDescription();
				statusCode = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getCode();
				updateResultsToFile(statusCode, description);
				System.out.println("\nThe Error Response: Code=" + statusCode + " Decription=" + description);
			}
		}
	  
		else if(isAcceptanceAuditPreCheckRequest)
		{
			try
			{
				transactionReference.setCustomerContext("JAX-WS Test Client Program for Acceptance Audit Pre-Check Request.");
				requestType.setTransactionReference(transactionReference);
			  
				AcceptanceAuditPreCheckRequest acceptanceAuditPreCheckReq = new AcceptanceAuditPreCheckRequest();
				ShipmentType shipment = new ShipmentType();
				
				shipment.setShipperNumber("Set your Shipper Number here.");
				
				/** Populate Ship From Address **/
				
				AddressType shipFromAddress = new AddressType();
				shipFromAddress.setAddressLine("Clayallee 170");
				shipFromAddress.setCity("Berlin");
				shipFromAddress.setPostalCode("14191");
				shipFromAddress.setCountryCode("DE");
				shipment.setShipFromAddress(shipFromAddress);
				
				
				/** Populate Ship From Address **/
				
				AddressType shipToAddress = new AddressType();
				shipToAddress.setAddressLine("Gieener Str");
				shipToAddress.setCity("Frankfurt");
				shipToAddress.setPostalCode("60435");
				shipToAddress.setCountryCode("DE");
				shipment.setShipToAddress(shipToAddress);
				
				
				/** Populate Service **/
				
				ServiceType service = new ServiceType();
				service.setCode("011");
				shipment.setService(service);
				
				/** Populate Regulation Set **/
				
				shipment.setRegulationSet("ADR");
				
				/** Populate Package Information **/
				List <PackageType> listPackageType = shipment.getPackage();
				
				PackageType packageInfo = new PackageType();
				packageInfo.setPackageIdentifier("1");
				packageInfo.setEmergencyPhone("1234567890");
				packageInfo.setEmergencyContact("Emergency Contact");
				packageInfo.setTransportationMode("GND");
				
				/** Populate Package Weight Information **/
				PackageWeightType packageWeight = new PackageWeightType();
				packageWeight.setWeight("1");
				
				/** Populate Package Weight Unit of Measurement Information **/
				PackageWeightUOMType uom = new PackageWeightUOMType();
				uom.setCode("KGS");
				packageWeight.setUnitOfMeasurement(uom);
				
				packageInfo.setPackageWeight(packageWeight);
				
				/** Populate Chemical Record Information **/
				List <ChemicalRecordType> listChemicalRecordType = packageInfo.getChemicalRecord();
				
				ChemicalRecordType chemicalRecord = new ChemicalRecordType();
				chemicalRecord.setChemicalRecordIdentifier("1");
				chemicalRecord.setClassDivisionNumber("3");
				chemicalRecord.setIDNumber("UN2561");
				chemicalRecord.setPackagingInstructionCode("P001");
				chemicalRecord.setProperShippingName("3-METHYL-1-BUTENE");
				chemicalRecord.setPackagingGroupType("I");
				chemicalRecord.setPackagingTypeQuantity("1");
				chemicalRecord.setPackagingType("Fiberboard Box");
				chemicalRecord.setQuantity("1");
				chemicalRecord.setUOM("L");
				chemicalRecord.setReportableQuantity("RQ");
				chemicalRecord.setCommodityRegulatedLevelCode("FR");
				chemicalRecord.setTransportCategory("1");
				chemicalRecord.setTunnelRestrictionCode("D/E");
				
				listChemicalRecordType.add(chemicalRecord);
				
				listPackageType.add(packageInfo);
				
				acceptanceAuditPreCheckReq.setShipment(shipment);
				
				AcceptanceAuditPreCheckResponse acceptanceAuditPreCheckRes = dangerousGoodsUtilityPort.processAcceptanceAuditPreCheck(acceptanceAuditPreCheckReq, upsSecurity);

				System.out.println("Transaction Status: " + acceptanceAuditPreCheckRes.getResponse().getResponseStatus().getDescription());
				
				if((acceptanceAuditPreCheckRes.getPackageResults() != null) && (acceptanceAuditPreCheckRes.getPackageResults().size() > 0))
				{
					int sizeOfPackageResults = acceptanceAuditPreCheckRes.getPackageResults().size();
					
					for(int i = 0; i < sizeOfPackageResults; i ++)
					{
						PackageResultsType packageResult = acceptanceAuditPreCheckRes.getPackageResults().get(i);
						
						System.out.println("PackageIdentifier : " + packageResult.getPackageIdentifier());
						
						System.out.println("For PackageIdentifier : " + packageResult.getPackageIdentifier() + " , AccessibleIndicator is " + packageResult.getAccessibleIndicator());
						
						System.out.println("For PackageIdentifier : " + packageResult.getPackageIdentifier() + " , EuropeBUIndicator is " + packageResult.getEuropeBUIndicator());
						
						if((packageResult.getChemicalRecordResults() != null) && (packageResult.getChemicalRecordResults().size() > 0))
						{
							int sizeOfChemicalRecordResults = packageResult.getChemicalRecordResults().size();
							
							for(int j = 0; j < sizeOfChemicalRecordResults; j ++)
							{
								ChemicalRecordResultsType chemicalRecordResult = packageResult.getChemicalRecordResults().get(j);
								
								System.out.println("ChemicalRecordIdentifier : " + chemicalRecordResult.getChemicalRecordIdentifier());
								
								System.out.println("For ChemicalRecordIdentifier : " + chemicalRecordResult.getChemicalRecordIdentifier() + " , ADRPoints are " + chemicalRecordResult.getADRPoints());
							}
						}
					}
				}
				statusCode = acceptanceAuditPreCheckRes.getResponse().getResponseStatus().getCode();
				description = acceptanceAuditPreCheckRes.getResponse().getResponseStatus().getDescription();
				updateResultsToFile(statusCode, description);
			}
			
			catch (AcceptanceAuditPreCheckErrorMessage e)
			{
				Errors faultMessage = e.getFaultInfo();
				description = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getDescription();
				statusCode = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getCode();
				updateResultsToFile(statusCode, description);
				System.out.println("\nThe Error Response: Code=" + statusCode + " Decription=" + description);
			}
		  
		}
			
	}

	/**
	 * This method updates the XOLTResult.xml file with the received status and
	 * description
	 * 
	 * @param statusCode
	 * @param description
	 */
	private static void updateResultsToFile(String statusCode,
			String description) {
		BufferedWriter bw = null;
		try {

			File outFile = new File(out_file_location);
			System.out.println("Output file deletion status: "
					+ outFile.delete());
			outFile.createNewFile();
			System.out.println("Output file location: "
					+ outFile.getCanonicalPath());
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
					bw = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean isNullOrEmpty(String input)
	{
		if((input == null) || (input == ""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static void loadProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(buildPropertiesPath));

		} catch (IOException e) {
			statusCode = e.getMessage();
			description = e.toString();
			updateResultsToFile(statusCode, description);
			e.printStackTrace();
		}
		url = properties.getProperty("url");
		accessKey = properties.getProperty("accesskey");
		userName = properties.getProperty("username");
		password = properties.getProperty("password");
		out_file_location = properties.getProperty("out_file_location");
		tool_or_webservice_name = properties
				.getProperty("tool_or_webservice_name");
	}

}




