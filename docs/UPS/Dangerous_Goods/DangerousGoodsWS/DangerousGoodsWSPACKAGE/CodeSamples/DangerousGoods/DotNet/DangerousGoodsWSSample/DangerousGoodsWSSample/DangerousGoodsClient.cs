using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DangerousGoodsWSSample.WebReference;

namespace DangerousGoodsWSSample
{
    class DangerousGoodsClient
    {
        static void Main(string[] args)
        {
            /*
		     * Set the boolean value true depending upon type of request {Either Acceptance Audit Pre-Check or Chemical Reference Data request}
		     */


            Boolean isAcceptanceAuditPreCheckRequest = false;   //For Acceptance Audit Pre-Check request, set the value to true.
            Boolean isChemicalReferenceDataRequest = true;    //For Chemical Reference Data request, set the value to true.

            /** ************UPSSE************************** */
            UPSSecurity upss = new UPSSecurity();
            UPSSecurityServiceAccessToken upssSvcAccessToken = new UPSSecurityServiceAccessToken();
            upssSvcAccessToken.AccessLicenseNumber = "Your Access License Number";
            upss.ServiceAccessToken = upssSvcAccessToken;
            UPSSecurityUsernameToken upssUsrNameToken = new UPSSecurityUsernameToken();
            upssUsrNameToken.Username = "Your User Name";
            upssUsrNameToken.Password = "Your Password";
            upss.UsernameToken = upssUsrNameToken;
            /** ************UPSSE************************** */

            if (isChemicalReferenceDataRequest)
            {
                try
                {
                    DangerousGoodsUtility dangerousGoodsUtilityService = new DangerousGoodsUtility();
                    ChemicalReferenceDataRequest chemicalReferenceDataReq = new ChemicalReferenceDataRequest();

                    // Create Request
                    RequestType request = new RequestType();
                    String[] requestOption = { " " };
                    request.RequestOption = requestOption;

                    // Create TransactionRef
                    TransactionReferenceType tranRef = new TransactionReferenceType();
                    tranRef.CustomerContext = "C# Test Client Program for Chemical Reference Data Request.";
                    request.TransactionReference = tranRef;
                    chemicalReferenceDataReq.Request = request;

                    chemicalReferenceDataReq.ShipperNumber = "Set your Shipper Number here.";
                    chemicalReferenceDataReq.ProperShippingName = "3-METHYL-1-BUTENE";
                    chemicalReferenceDataReq.IDNumber = "UN2561";

                    dangerousGoodsUtilityService.UPSSecurityValue = upss;

                    System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12 | System.Net.SecurityProtocolType.Tls | System.Net.SecurityProtocolType.Tls11; //This line will ensure the latest security protocol for consuming the web service call.
                    Console.WriteLine(chemicalReferenceDataReq);

                    ChemicalReferenceDataResponse chemicalReferenceDataRes = dangerousGoodsUtilityService.ProcessChemicalReferenceData(chemicalReferenceDataReq);
                    Console.WriteLine("Status Code : " + chemicalReferenceDataRes.Response.ResponseStatus.Code);
                    Console.WriteLine("Status Description: " + chemicalReferenceDataRes.Response.ResponseStatus.Description);

                    if ((chemicalReferenceDataRes.ChemicalData != null) && (chemicalReferenceDataRes.ChemicalData.Length > 0))
                    {
                        int chemicalDataSize = chemicalReferenceDataRes.ChemicalData.Length;
                        ChemicalDataType[] chemicalDataTypeStrArr = chemicalReferenceDataRes.ChemicalData;

                        for (int i = 0; i < chemicalDataSize; i++)
                        {
                            ChemicalDataType chemData = chemicalDataTypeStrArr[i];
                            if (chemData != null)
                            {
                                if (chemData.ChemicalDetail != null)
                                {
                                    ChemicalDetailType chemDetailType = chemData.ChemicalDetail;
                                    if ((chemDetailType.ClassDivisionNumber != null) && (chemDetailType.ClassDivisionNumber != ""))
                                    {
                                        Console.WriteLine("ClassDivisonNumber for ChemicalData  " + i + " is " + chemDetailType.ClassDivisionNumber);
                                    }

                                    if ((chemDetailType.IDNumber != null) && (chemDetailType.IDNumber != ""))
                                    {
                                        Console.WriteLine("IDNumber for ChemicalData  " + i + " is " + chemDetailType.IDNumber);
                                    }

                                    if ((chemDetailType.HazardousMaterialsDescription != null) && (chemDetailType.HazardousMaterialsDescription != ""))
                                    {
                                        Console.WriteLine("HazardousMaterialsDescription for ChemicalData  " + i + " is " + chemDetailType.HazardousMaterialsDescription);
                                    }

                                    if ((chemDetailType.PackagingGroupType != null) && (chemDetailType.PackagingGroupType != ""))
                                    {
                                        Console.WriteLine("PackagingGroupType for ChemicalData  " + i + " is " + chemDetailType.PackagingGroupType);
                                    }

                                    if ((chemDetailType.SubRiskClass != null) && (chemDetailType.SubRiskClass != ""))
                                    {
                                        Console.WriteLine("SubRiskClass for ChemicalData  " + i + " is " + chemDetailType.SubRiskClass);
                                    }
                                }
                            }
                        }
                    }
                     Console.ReadKey();

                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.WriteLine("");
                    Console.WriteLine("---------Dangerous Goods Utility Web Service returns error----------------");
                    Console.WriteLine("---------\"Hard\" is user error \"Transient\" is system error----------------");
                    Console.WriteLine("SoapException Message= " + ex.Message);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException Category:Code:Message= " + ex.Detail.LastChild.InnerText);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException XML String for all= " + ex.Detail.LastChild.OuterXml);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException StackTrace= " + ex.StackTrace);
                    Console.WriteLine("-------------------------");
                    Console.WriteLine("");
                }
                catch (System.ServiceModel.CommunicationException er)
                {
                    Console.WriteLine("");
                    Console.WriteLine("--------------------");
                    Console.WriteLine("CommunicationException= " + er.Message);
                    Console.WriteLine("CommunicationException-StackTrace= " + er.StackTrace);
                    Console.WriteLine("-------------------------");
                    Console.WriteLine("");
                }
                catch (Exception e)
                {
                    Console.WriteLine("");
                    Console.WriteLine("-------------------------");
                    Console.WriteLine(" Generaal Exception= " + e.Message);
                    Console.WriteLine(" Generaal Exception-StackTrace= " + e.StackTrace);
                    Console.WriteLine("-------------------------");
                }
                finally
                {
                    Console.ReadKey();
                }

            }
            else if (isAcceptanceAuditPreCheckRequest)
            {
                try
                {
                    DangerousGoodsUtility dangerousGoodsUtilityService = new DangerousGoodsUtility();
                    AcceptanceAuditPreCheckRequest acceptanceAuditPreCheckReq = new AcceptanceAuditPreCheckRequest();


                    // Create Request
                    RequestType request = new RequestType();
                    String[] requestOption = { " " };
                    request.RequestOption = requestOption;

                    // Create TransactionRef
                    TransactionReferenceType tranRef = new TransactionReferenceType();
                    tranRef.CustomerContext = "C# TestClient Program for UPS Paperless Document API WebServices";
                    request.TransactionReference = tranRef;
                    acceptanceAuditPreCheckReq.Request = request;

                    ShipmentType shipment = new ShipmentType();

                    shipment.ShipperNumber = "Set your Shipper Number here.";

                    // Populate Ship From Address
                    AddressType shipFromAddress = new AddressType();
                    shipFromAddress.AddressLine = "Clayallee 170";
                    shipFromAddress.City = "Berlin";
                    shipFromAddress.PostalCode = "14191";
                    shipFromAddress.CountryCode = "DE";
                    shipment.ShipFromAddress = shipFromAddress;

                    // Populate Ship To Address
                    AddressType shipToAddress = new AddressType();
                    shipToAddress.AddressLine = "Gieener Str";
                    shipToAddress.City = "Frankfurt";
                    shipToAddress.PostalCode = "60435";
                    shipToAddress.CountryCode = "DE";
                    shipment.ShipToAddress = shipToAddress;

                    // Populate Service
                    ServiceType service = new ServiceType();
                    service.Code = "011";
                    shipment.Service = service;

                    // Populate Regulation Set
                    shipment.RegulationSet = "ADR";

                    // Populate Package Information
                    List<PackageType> listPackageType = new List<PackageType>();

                    PackageType packageInfo = new PackageType();
                    packageInfo.PackageIdentifier = "1";
                    packageInfo.EmergencyContact = "Emergency Contact";
                    packageInfo.EmergencyPhone = "1234567890";
                    packageInfo.TransportationMode = "GND";

                    // Populate Package Weight Information
                    PackageWeightType packageWeight = new PackageWeightType();
                    packageWeight.Weight = "1";

                    // Populate Package Weight Unit Of Measurement Information
                    PackageWeightUOMType uom = new PackageWeightUOMType();
                    uom.Code = "KGS";
                    packageWeight.UnitOfMeasurement = uom;

                    packageInfo.PackageWeight = packageWeight;

                    // Populate Chemical Record Information
                    List<ChemicalRecordType> listChemicalRecordType = new List<ChemicalRecordType>();

                    ChemicalRecordType chemicalRecord = new ChemicalRecordType();
                    chemicalRecord.ChemicalRecordIdentifier = "1";
                    chemicalRecord.ClassDivisionNumber = "3";
                    chemicalRecord.IDNumber = "UN2561";
                    chemicalRecord.PackagingInstructionCode = "P001";
                    chemicalRecord.ProperShippingName = "3-METHYL-1-BUTENE";
                    chemicalRecord.PackagingGroupType = "I";
                    chemicalRecord.PackagingTypeQuantity = "1";
                    chemicalRecord.Quantity = "1";
                    chemicalRecord.UOM = "L";
                    chemicalRecord.ReportableQuantity = "RQ";
                    chemicalRecord.CommodityRegulatedLevelCode = "FR";
                    chemicalRecord.TransportCategory = "1";
                    chemicalRecord.TunnelRestrictionCode = "D/E";

                    listChemicalRecordType.Add(chemicalRecord);

                    ChemicalRecordType[] chemicalRecordStrArr = listChemicalRecordType.ToArray();
                    packageInfo.ChemicalRecord = chemicalRecordStrArr;

                    listPackageType.Add(packageInfo);

                    PackageType[] packageTypeStrArr = listPackageType.ToArray();
                    shipment.Package = packageTypeStrArr;

                    acceptanceAuditPreCheckReq.Shipment = shipment;

                    dangerousGoodsUtilityService.UPSSecurityValue = upss;

                    System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12 | System.Net.SecurityProtocolType.Tls | System.Net.SecurityProtocolType.Tls11; //This line will ensure the latest security protocol for consuming the web service call.
                    Console.WriteLine(acceptanceAuditPreCheckReq);

                    AcceptanceAuditPreCheckResponse acceptanceAuditPreCheckRes = dangerousGoodsUtilityService.ProcessAcceptanceAuditPreCheck(acceptanceAuditPreCheckReq);
                    Console.WriteLine("Status Code : " + acceptanceAuditPreCheckRes.Response.ResponseStatus.Code);
                    Console.WriteLine("Status Description: " + acceptanceAuditPreCheckRes.Response.ResponseStatus.Description);

                    if (acceptanceAuditPreCheckRes.PackageResults.Length > 0)
                    {
                        int lengthOfPackageResults = acceptanceAuditPreCheckRes.PackageResults.Length;
                        PackageResultsType[] packageResultsTypeStrArr = acceptanceAuditPreCheckRes.PackageResults;
                        for (int i = 0; i < lengthOfPackageResults; i++)
                        {
                            PackageResultsType packageResult = packageResultsTypeStrArr[i];
                            if (packageResult != null)
                            {
                                Console.WriteLine("PackageIdentifier : " + packageResult.PackageIdentifier);

                                Console.WriteLine("For PackageIdentifier : " + packageResult.PackageIdentifier + " , AccessibleIndicator is " + packageResult.AccessibleIndicator);

                                Console.WriteLine("For PackageIdentifier : " + packageResult.PackageIdentifier + " , EuropeBUIndicator is " + packageResult.EuropeBUIndicator);

                                if ((packageResult.ChemicalRecordResults != null) && (packageResult.ChemicalRecordResults.Length > 0))
                                {
                                    int lengthOfChemicalRecordResults = packageResult.ChemicalRecordResults.Length;
                                    ChemicalRecordResultsType[] chemicalRecordsResultsTypeStrArr = packageResult.ChemicalRecordResults;
                                    for (int j = 0; j < lengthOfPackageResults; j++)
                                    {
                                        ChemicalRecordResultsType chemicalRecordResult = chemicalRecordsResultsTypeStrArr[j];

                                        Console.WriteLine("ChemicalRecordIdentifier : " + chemicalRecordResult.ChemicalRecordIdentifier);

                                        Console.WriteLine("For ChemicalRecordIdentifier : " + chemicalRecordResult.ChemicalRecordIdentifier + " , ADRPoints are " + chemicalRecordResult.ADRPoints);
                                    }
                                }
                            }
                        }
                    }

                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.WriteLine("");
                    Console.WriteLine("---------Dangerous Goods Utility Web Service returns error----------------");
                    Console.WriteLine("---------\"Hard\" is user error \"Transient\" is system error----------------");
                    Console.WriteLine("SoapException Message= " + ex.Message);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException Category:Code:Message= " + ex.Detail.LastChild.InnerText);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException XML String for all= " + ex.Detail.LastChild.OuterXml);
                    Console.WriteLine("");
                    Console.WriteLine("SoapException StackTrace= " + ex.StackTrace);
                    Console.WriteLine("-------------------------");
                    Console.WriteLine("");

                }
                catch (System.ServiceModel.CommunicationException er)
                {
                    Console.WriteLine("");
                    Console.WriteLine("--------------------");
                    Console.WriteLine("CommunicationException= " + er.Message);
                    Console.WriteLine("CommunicationException-StackTrace= " + er.StackTrace);
                    Console.WriteLine("-------------------------");
                    Console.WriteLine("");
                }
                catch (Exception e)
                {
                    Console.WriteLine("");
                    Console.WriteLine("-------------------------");
                    Console.WriteLine(" Generaal Exception= " + e.Message);
                    Console.WriteLine(" Generaal Exception-StackTrace= " + e.StackTrace);
                    Console.WriteLine("-------------------------");
                }
                finally
                {
                    Console.ReadKey();
                }
            }
        }

        Boolean isNullOrEmpty(String input)
        {
            if ((input != null) && (input != ""))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
