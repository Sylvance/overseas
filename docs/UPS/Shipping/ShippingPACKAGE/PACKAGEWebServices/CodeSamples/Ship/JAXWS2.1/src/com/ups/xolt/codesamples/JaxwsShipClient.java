/*
 **
 ** Filename: JaxwsShipClient.java
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

import com.ups.xmlschema.xoltws.ship.v1.AddressType;
import com.ups.xmlschema.xoltws.ship.v1.BillShipperType;
import com.ups.xmlschema.xoltws.ship.v1.ContactType;
import com.ups.xmlschema.xoltws.ship.v1.DimensionsType;
import com.ups.xmlschema.xoltws.ship.v1.Errors;
import com.ups.xmlschema.xoltws.ship.v1.IFChargesType;
import com.ups.xmlschema.xoltws.ship.v1.InternationalFormType;
import com.ups.xmlschema.xoltws.ship.v1.LabelImageFormatType;
import com.ups.xmlschema.xoltws.ship.v1.LabelSpecificationType;
import com.ups.xmlschema.xoltws.ship.v1.OtherChargesType;
import com.ups.xmlschema.xoltws.ship.v1.PackageType;
import com.ups.xmlschema.xoltws.ship.v1.PackageWeightType;
import com.ups.xmlschema.xoltws.ship.v1.PackagingType;
import com.ups.xmlschema.xoltws.ship.v1.PaymentInfoType;
import com.ups.xmlschema.xoltws.ship.v1.PhoneType;
import com.ups.xmlschema.xoltws.ship.v1.ProductType;
import com.ups.xmlschema.xoltws.ship.v1.ProductWeightType;
import com.ups.xmlschema.xoltws.ship.v1.RequestType;
import com.ups.xmlschema.xoltws.ship.v1.ServiceType;
import com.ups.xmlschema.xoltws.ship.v1.ShipAddressType;
import com.ups.xmlschema.xoltws.ship.v1.ShipFromType;
import com.ups.xmlschema.xoltws.ship.v1.ShipPhoneType;
import com.ups.xmlschema.xoltws.ship.v1.ShipPortType;
import com.ups.xmlschema.xoltws.ship.v1.ShipService;
import com.ups.xmlschema.xoltws.ship.v1.ShipToAddressType;
import com.ups.xmlschema.xoltws.ship.v1.ShipToType;
import com.ups.xmlschema.xoltws.ship.v1.ShipUnitOfMeasurementType;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentChargeType;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentErrorMessage;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentRequest;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentResponse;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentType;
import com.ups.xmlschema.xoltws.ship.v1.ShipmentType.ShipmentServiceOptions;
import com.ups.xmlschema.xoltws.ship.v1.ShipperType;
import com.ups.xmlschema.xoltws.ship.v1.SoldToType;
import com.ups.xmlschema.xoltws.ship.v1.TransactionReferenceType;
import com.ups.xmlschema.xoltws.ship.v1.UPSSecurity;
import com.ups.xmlschema.xoltws.ship.v1.UPSSecurity.ServiceAccessToken;
import com.ups.xmlschema.xoltws.ship.v1.UPSSecurity.UsernameToken;
import com.ups.xmlschema.xoltws.ship.v1.UnitOfMeasurementType;
import com.ups.xmlschema.xoltws.ship.v1.UnitType;

public class JaxwsShipClient {
	private static String url;
	private static String accessKey;
	private static String userName;
	private static String password;
	private static String buildPropertiesPath = "./build.properties";
	private static String out_file_location = "out_file_location";
	private static String tool_or_webservice_name = "tool_or_webservice_name";
	private static String statusCode = null;
	private static String description = null;

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

	public static void main(String[] arguments) throws Exception {
		try {
			loadProperties();
			ShipService shipService = new ShipService();
			ShipPortType shipPort = shipService.getShipPort();
			BindingProvider bp = (BindingProvider) shipPort;
			bp.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
			ShipmentRequest shipRequest = new ShipmentRequest();

			RequestType requestType = new RequestType();
			TransactionReferenceType transactionReference = new TransactionReferenceType();
			transactionReference.setCustomerContext("JAX-WS Test Client");
			requestType.setTransactionReference(transactionReference);
			List<String> requestOption = requestType.getRequestOption();
			requestOption.add("nonvalidate");
			shipRequest.setRequest(requestType);

			ShipmentType shpmnt = new ShipmentType();


			/** *******Shipper******************** */
			ShipperType shipper = new ShipperType();
			shipper.setName("ABC Associates");
			shipper.setAttentionName("ShipperZs Attn Name");
			shipper.setTaxIdentificationNumber("123456");

			ShipPhoneType shipperPhone = new ShipPhoneType();
			shipperPhone.setNumber("1234567898");
			shipperPhone.setExtension("1");
			shipper.setPhone(shipperPhone);

			shipper.setShipperNumber("Your Shipper Number");
			shipper.setFaxNumber("8002222222");

			ShipAddressType shipperAddress = new ShipAddressType();
			List<String> addressLineList = shipperAddress.getAddressLine();
			addressLineList.add("34 Queen St");

			shipperAddress.setCity("Timonium");
			shipperAddress.setPostalCode("21093");
			shipperAddress.setStateProvinceCode("MD");
			shipperAddress.setCountryCode("US");
			shipper.setAddress(shipperAddress);


			shpmnt.setShipper(shipper);
			/** ******Shipper********************* */

			/** ************ShipTo****************** */
			ShipToType shipTo = new ShipToType();
			shipTo.setName("Happy Dog Pet Supply");
			shipTo.setAttentionName("Ship To Attention Name");

			ShipPhoneType shipToPhone = new ShipPhoneType();
			shipToPhone.setNumber("1234567898");
			shipTo.setPhone(shipToPhone);

			ShipToAddressType shipToAddress = new ShipToAddressType();
			List<String> ShipToAddressLineList = shipToAddress.getAddressLine();
			ShipToAddressLineList.add("GOERLITZER STR.1");
			shipToAddress.setCity("Neuss");
			shipToAddress.setPostalCode("41456");
			shipToAddress.setCountryCode("DE");
			shipTo.setAddress(shipToAddress);
			shpmnt.setShipTo(shipTo);
			/** ***********ShipTo********************* */

			/** ************ShipFrom****************** */
			ShipFromType shipFrom = new ShipFromType();
			shipFrom.setName("ABC Associates");
			shipFrom.setAttentionName("Mr.ABC");
			ShipAddressType shipFromAddress = new ShipAddressType();
			List<String> addressLineList_shipFrom = shipFromAddress
					.getAddressLine();
			addressLineList_shipFrom.add("34 Queen St");
			shipFromAddress.setCity("Timonium");
			shipFromAddress.setPostalCode("21093");
			shipFromAddress.setStateProvinceCode("MD");
			shipFromAddress.setCountryCode("US");
			shipFrom.setAddress(shipFromAddress);
			shpmnt.setShipFrom(shipFrom);
			/** ***********ShipFrom********************* */

			/** *************Payment Information***************** */
			PaymentInfoType payInfo = new PaymentInfoType();
			ShipmentChargeType shpmntCharge = new ShipmentChargeType();
			shpmntCharge.setType("01");
			BillShipperType billShipper = new BillShipperType();
			billShipper.setAccountNumber("Your Account Number");
			shpmntCharge.setBillShipper(billShipper);
			ShipmentChargeType[] shpmntChargeArray = { shpmntCharge };
			List shipmentChargesList = payInfo.getShipmentCharge();
			shipmentChargesList.add(shpmntChargeArray[0]);
			shpmnt.setPaymentInformation(payInfo);
			/** *************Payment Information***************** */

			/** ********Service********************** */
			ServiceType service = new ServiceType();
			service.setCode("08");
			service.setDescription("UPS Expedited");
			shpmnt.setService(service);
			/** ********Service********************** */

			/** **** Shipment Service Options ***** */
			ShipmentServiceOptions shpServiceOptions = new ShipmentServiceOptions();

			/** **** International Forms ***** */
			InternationalFormType internationalForms = new InternationalFormType();
			List<String> formTypeList = internationalForms.getFormType();

			/** **** Commercial Invoice ***** */
			formTypeList.add("01");

			/** **** Contacts and Sold To ***** */
			ContactType contacts = new ContactType();

			SoldToType soldTo = new SoldToType();
			soldTo.setOption("01");
			soldTo.setAttentionName("Sold To Attn Name");
			soldTo.setName("Sold To Name");
			PhoneType soldToPhone = new PhoneType();
			soldToPhone.setNumber("1234567890");
			soldToPhone.setExtension("1234");
			soldTo.setPhone(soldToPhone);
			AddressType soldToAddress = new AddressType();
			List<String> addressLineList_soldTo = soldToAddress.getAddressLine();
			addressLineList_soldTo.add("34 Queen St");
			soldToAddress.setCity("Frankfurt");
			soldToAddress.setPostalCode("60547");
			soldToAddress.setCountryCode("DE");
			soldTo.setAddress(soldToAddress);
			contacts.setSoldTo(soldTo);

			internationalForms.setContacts(contacts);

			/** **** Product ***** */
			List<ProductType> productList = internationalForms.getProduct();
			ProductType product = new ProductType();
			List<String> descriptionList = product.getDescription();
			descriptionList.add("Product 1");
			product.setCommodityCode("111222AA");
			product.setOriginCountryCode("US");
			UnitType unit = new UnitType();
			unit.setNumber("147");
			unit.setValue("478");
			UnitOfMeasurementType uom = new UnitOfMeasurementType();
			uom.setCode("BOX");
			uom.setDescription("BOX");
			unit.setUnitOfMeasurement(uom);
			product.setUnit(unit);
			ProductWeightType productWeight = new ProductWeightType();
			productWeight.setWeight("10");
			UnitOfMeasurementType uomForWeight = new UnitOfMeasurementType();
			uomForWeight.setCode("LBS");
			uomForWeight.setDescription("LBS");
			productWeight.setUnitOfMeasurement(uomForWeight);
			product.setProductWeight(productWeight);
			productList.add(product);

			/** **** InvoiceNumber, InvoiceDate, PurchaseOrderNumber, TermsOfShipment, ReasonForExport, Comments and DeclarationStatement  ***** */
			internationalForms.setInvoiceNumber("asdf123");
			internationalForms.setInvoiceDate("20151225");
			internationalForms.setPurchaseOrderNumber("999jjj777");
			internationalForms.setTermsOfShipment("CFR");
			internationalForms.setReasonForExport("Sale");
			internationalForms.setComments("Your Comments");
			internationalForms.setDeclarationStatement("Your Declaration Statement");

			/** **** Discount, FreightCharges, InsuranceCharges, OtherCharges and CurrencyCode  ***** */
			IFChargesType discount = new IFChargesType();
			discount.setMonetaryValue("100");
			internationalForms.setDiscount(discount);
			IFChargesType freight = new IFChargesType();
			freight.setMonetaryValue("50");
			internationalForms.setFreightCharges(freight);
			IFChargesType insurance = new IFChargesType();
			insurance.setMonetaryValue("200");
			internationalForms.setInsuranceCharges(insurance);
			OtherChargesType otherCharges = new OtherChargesType();
			otherCharges.setMonetaryValue("50");
			otherCharges.setDescription("Misc");
			internationalForms.setOtherCharges(otherCharges);
			internationalForms.setCurrencyCode("USD");


			shpServiceOptions.setInternationalForms(internationalForms);
			shpmnt.setShipmentServiceOptions(shpServiceOptions);
			/** ******************Package***************** */
			PackageType pkg1 = new PackageType();
			PackagingType pkgingType = new PackagingType();
			pkgingType.setCode("02");
			pkgingType.setDescription("Nail");
			pkg1.setPackaging(pkgingType);

			DimensionsType dimensionsType = new DimensionsType();
			ShipUnitOfMeasurementType unitOfMeas = new ShipUnitOfMeasurementType();
			unitOfMeas.setCode("IN");
			unitOfMeas.setDescription("Inches");
			dimensionsType.setUnitOfMeasurement(unitOfMeas);
			dimensionsType.setLength("20");
			dimensionsType.setWidth("25");
			dimensionsType.setHeight("25");
			pkg1.setDimensions(dimensionsType);

			PackageType[] pkgArray = { pkg1 };
			PackageWeightType weight = new PackageWeightType();
			weight.setWeight("10");
			ShipUnitOfMeasurementType shpUnitOfMeas = new ShipUnitOfMeasurementType();
			shpUnitOfMeas.setCode("LBS");
			shpUnitOfMeas.setDescription("Pounds");
			weight.setUnitOfMeasurement(shpUnitOfMeas);
			pkg1.setPackageWeight(weight);
			List packageList = shpmnt.getPackage();
			packageList.add(pkgArray[0]);

			/** ******************Package***************** */

			/** **********Label Specification ******************** */
			LabelSpecificationType labelSpecType = new LabelSpecificationType();
			LabelImageFormatType labelImageFormat = new LabelImageFormatType();
			labelImageFormat.setCode("GIF");
			labelImageFormat.setDescription("GIF");
			labelSpecType.setLabelImageFormat(labelImageFormat);
			labelSpecType.setHTTPUserAgent("Mozilla/4.5");
			shipRequest.setLabelSpecification(labelSpecType);
			/** ***********Label Specification********************* */

			shpmnt.setDescription("Some Goods");
			shipRequest.setShipment(shpmnt);

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
			System.out.println(shipRequest);
			ShipmentResponse shipResponse = shipPort.processShipment(
					shipRequest, upsSecurity);

			System.out.println("Transaction Status: "
					+ shipResponse.getResponse().getResponseStatus()
							.getDescription());
			System.out.println("Shipment 1Z number: "
					+ shipResponse.getShipmentResults()
							.getShipmentIdentificationNumber());
			System.out.println(shipResponse.getShipmentResults()
					.getPackageResults().get(0).getShippingLabel()
					.getGraphicImage());

			statusCode = shipResponse.getResponse().getResponseStatus()
					.getCode();
			description = shipResponse.getResponse().getResponseStatus()
					.getDescription();
			updateResultsToFile(statusCode, description);

		} catch (Exception e) {

			if(e instanceof ShipmentErrorMessage){
				ShipmentErrorMessage err = (ShipmentErrorMessage)e;
				Errors faultMessage = err.getFaultInfo();
				description = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getDescription();
				statusCode = faultMessage.getErrorDetail().get(0).getPrimaryErrorCode().getCode();
				updateResultsToFile(statusCode, description);
				System.out.println("\nThe Error Response: Code=" + statusCode
					+ " Decription=" + description);
			}
			else{
				statusCode = e.getMessage();
				description = e.toString();
				updateResultsToFile(statusCode, description);
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
}
