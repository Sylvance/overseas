package com.ups.xolt.codesamples;

/*
 **
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.AcceptanceAuditPreCheckErrorMessage;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.ChemicalReferenceDataErrorMessage;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.AcceptanceAuditPreCheckResponse;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.AddressType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ChemicalDataType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ChemicalDetailType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ChemicalRecordResultsType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ChemicalRecordType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ChemicalReferenceDataResponse;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.PackageResultsType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.PackageType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.PackageWeightType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.PackageWeightUOMType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.RequestType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ServiceAccessToken_type0;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ServiceType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.ShipmentType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.TransactionReferenceType;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.UPSSecurity;
import com.ups.www.wsdl.xoltws.dangerousgoodsutility.v1_0.DangerousGoodsUtilityStub.UsernameToken_type0;

public class Axis2DangerousGoodsClient {
	
	private static String url;
	private static String accesskey;
	private static String username;
	private static String password;
	private static String out_file_location = "out_file_location";
	private static String tool_or_webservice_name = "tool_or_webservice_name";
	static Properties props = null;
	
	public static void main(String[] args)
	{
		String statusCode = null;
		String description = null;

		/*
		 * Set the boolean value true depending upon type of request {Either Acceptance Audit Pre-Check or Chemical Reference Data request}
		 */
		boolean isAcceptanceAuditPreCheckRequest = false; //For Acceptance Audit Pre-Check request, set the value to true.
		boolean isChemicalReferenceDataRequest = true;  //For Chemical Reference Data request, set the value to true.
		
		RequestType request = new RequestType();
		TransactionReferenceType transactionReference = new TransactionReferenceType();
		
		/** ************UPSSE************************** */
		UPSSecurity upss = new UPSSecurity();
		ServiceAccessToken_type0 upsSvcToken = new ServiceAccessToken_type0();
		upsSvcToken.setAccessLicenseNumber(accesskey);
		upss.setServiceAccessToken(upsSvcToken);
		UsernameToken_type0 upsSecUsrnameToken = new UsernameToken_type0();
		upsSecUsrnameToken.setUsername(username);
		upsSecUsrnameToken.setPassword(password);
		upss.setUsernameToken(upsSecUsrnameToken);
		/** ************UPSSE***************************** */

		if(isChemicalReferenceDataRequest)
		{
			try
			{
				DangerousGoodsUtilityStub dangerousGoodsUtilityService = new DangerousGoodsUtilityStub(url);
				DangerousGoodsUtilityStub.ChemicalReferenceDataRequest chemicalReferenceDataReq = new DangerousGoodsUtilityStub.ChemicalReferenceDataRequest();
				transactionReference.setCustomerContext("AXIS Test Client Program for Chemical Reference Data Request.");
				request.setTransactionReference(transactionReference);
				chemicalReferenceDataReq.setRequest(request);
				
				chemicalReferenceDataReq.setShipperNumber("Set your Shipper Number here.");
				
				chemicalReferenceDataReq.setIDNumber("UN2561");
				chemicalReferenceDataReq.setProperShippingName("3-METHYL-1-BUTENE");
				
				ChemicalReferenceDataResponse chemicalReferenceDataRes = dangerousGoodsUtilityService.ProcessChemicalReferenceData(chemicalReferenceDataReq, upss);
				System.out.println("Transaction Status: " + chemicalReferenceDataRes.getResponse().getResponseStatus().getDescription());
				
				if((chemicalReferenceDataRes.getChemicalData() != null) && (chemicalReferenceDataRes.getChemicalData().length > 0))
				{
					int chemicalDataSize = chemicalReferenceDataRes.getChemicalData().length;
					ChemicalDataType[] chemicalDataArray = chemicalReferenceDataRes.getChemicalData();
					for(int i=0; i < chemicalDataSize; i++)
					{
						ChemicalDataType chemData = chemicalDataArray[i];
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
			catch(ChemicalReferenceDataErrorMessage er)
			{
				description=er.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription();
				statusCode=er.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode();
				updateResultsToFile(statusCode, description);
				System.out.println("\nThe Error Response: Code=" + statusCode + " Decription=" + description);
			}
			catch(Exception e)
			{
				// Unexpected
				description = e.getMessage();
				updateResultsToFile("Error", description);
				System.out.println("********* The Failure= \n");
				e.printStackTrace();
			}

		}
		else if(isAcceptanceAuditPreCheckRequest)
		{
			try
			{
				DangerousGoodsUtilityStub dangerousGoodsUtilityService = new DangerousGoodsUtilityStub(url);
				DangerousGoodsUtilityStub.AcceptanceAuditPreCheckRequest acceptanceAuditPreCheckReq = new DangerousGoodsUtilityStub.AcceptanceAuditPreCheckRequest();
				transactionReference.setCustomerContext("AXIS Test Client Program for Acceptance Audit Pre-Check Request.");
				request.setTransactionReference(transactionReference);
				acceptanceAuditPreCheckReq.setRequest(request);
				
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
				ArrayList <PackageType> listPackageType = new ArrayList <PackageType>();
				
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
				ArrayList <ChemicalRecordType> listChemicalRecordType = new ArrayList <ChemicalRecordType>();
				
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
				ChemicalRecordType[] chemicalRecordTypeArray = listChemicalRecordType.toArray(new ChemicalRecordType[listChemicalRecordType.size()]);
				packageInfo.setChemicalRecord(chemicalRecordTypeArray);
				
				listPackageType.add(packageInfo);
				PackageType[] packageTypeArray = listPackageType.toArray(new PackageType[listPackageType.size()]);
				shipment.setPackage(packageTypeArray);
				
				acceptanceAuditPreCheckReq.setShipment(shipment);
				
				AcceptanceAuditPreCheckResponse acceptanceAuditPreCheckRes = dangerousGoodsUtilityService.ProcessAcceptanceAuditPreCheck(acceptanceAuditPreCheckReq, upss);
				
				System.out.println("Transaction Status: " + acceptanceAuditPreCheckRes.getResponse().getResponseStatus().getDescription());
				
				if((acceptanceAuditPreCheckRes.getPackageResults() != null) && (acceptanceAuditPreCheckRes.getPackageResults().length > 0))
				{
					int sizeOfPackageResults = acceptanceAuditPreCheckRes.getPackageResults().length;
					PackageResultsType[] packageResultsType = acceptanceAuditPreCheckRes.getPackageResults();
					
					for(int i = 0; i < sizeOfPackageResults; i ++)
					{
						PackageResultsType packageResult = packageResultsType[i];
						
						System.out.println("PackageIdentifier : " + packageResult.getPackageIdentifier());
						
						System.out.println("For PackageIdentifier : " + packageResult.getPackageIdentifier() + " , AccessibleIndicator is " + packageResult.getAccessibleIndicator());
						
						System.out.println("For PackageIdentifier : " + packageResult.getPackageIdentifier() + " , EuropeBUIndicator is " + packageResult.getEuropeBUIndicator());
						
						if((packageResult.getChemicalRecordResults() != null) && (packageResult.getChemicalRecordResults().length > 0))
						{
							int sizeOfChemicalRecordResults = packageResult.getChemicalRecordResults().length;
							
							ChemicalRecordResultsType[] chemicalRecordResultsType = packageResult.getChemicalRecordResults();
							
							for(int j = 0; j < sizeOfChemicalRecordResults; j ++)
							{
								ChemicalRecordResultsType chemicalRecordResult = chemicalRecordResultsType[j];
								
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
			catch(AcceptanceAuditPreCheckErrorMessage er)
			{
				description=er.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getDescription();
				statusCode=er.getFaultMessage().getErrorDetail()[0].getPrimaryErrorCode().getCode();
				updateResultsToFile(statusCode, description);
				System.out.println("\nThe Error Response: Code=" + statusCode + " Decription=" + description);
			}
			catch(Exception e)
			{
				// Unexpected
				description = e.getMessage();
				updateResultsToFile("Error", description);
				System.out.println("********* The Failure= \n");
				e.printStackTrace();
			}
		}
	}
	
	static 
	{
		try 
		{
			props = new Properties();
			props.load(new FileInputStream("./build.properties"));
			url = props.getProperty("url");
			accesskey = props.getProperty("accesskey");
			username = props.getProperty("username");
			password = props.getProperty("password");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void updateResultsToFile(String statusCode,String description) 
	{
		BufferedWriter bw = null;
    	try
    	{
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
    	}
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    	finally
    	{
    		try
    		{
				if (bw != null)
				{
					bw.close();
					bw = null;
				}
			}
    		catch (Exception e) 
    		{
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

}
