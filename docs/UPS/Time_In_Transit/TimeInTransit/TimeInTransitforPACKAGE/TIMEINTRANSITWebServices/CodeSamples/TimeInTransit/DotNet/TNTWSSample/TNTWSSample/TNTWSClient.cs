using System;
using System.Collections.Generic;
using System.Text;
using TNTWSSample.TNTWebReference;
using System.ServiceModel;

namespace TNTWSSample
{
    class TNTWSClient
    {
        static void Main()
        {
            try
            {
                TimeInTransitService tntService = new TimeInTransitService();
                TimeInTransitRequest tntRequest = new TimeInTransitRequest();
                RequestType request = new RequestType();
                String[] requestOption = { "TNT" };
                request.RequestOption = requestOption;
                tntRequest.Request = request;

                RequestShipFromType shipFrom = new RequestShipFromType();
                RequestShipFromAddressType addressFrom = new RequestShipFromAddressType();
                addressFrom.City = "ShipFrom city";
                addressFrom.CountryCode = "ShipFrom country";
                addressFrom.PostalCode = "ShipFrom postal code";
                addressFrom.StateProvinceCode = "ShipFrom state province code";
                shipFrom.Address = addressFrom;
                tntRequest.ShipFrom = shipFrom;

                RequestShipToType shipTo = new RequestShipToType();
                RequestShipToAddressType addressTo = new RequestShipToAddressType();
                addressTo.City = "ShipTo city";
                addressTo.CountryCode = "ShipTo country code";
                addressTo.PostalCode = "ShipTo postal code";
                addressTo.StateProvinceCode = "ShipTo state province code";
                shipTo.Address = addressTo;
                tntRequest.ShipTo = shipTo;

                PickupType pickup = new PickupType();
                pickup.Date = "Your pickup date";
                tntRequest.Pickup = pickup;

                //Below code uses dummy data for reference. Please update as required.
                ShipmentWeightType shipmentWeight = new ShipmentWeightType();
                shipmentWeight.Weight = "10";
                CodeDescriptionType unitOfMeasurement = new CodeDescriptionType();
                unitOfMeasurement.Code = "KGS";
                unitOfMeasurement.Description = "Kilograms";
                shipmentWeight.UnitOfMeasurement = unitOfMeasurement;
                tntRequest.ShipmentWeight = shipmentWeight;

                tntRequest.TotalPackagesInShipment = "1";
                InvoiceLineTotalType invoiceLineTotal = new InvoiceLineTotalType();
                invoiceLineTotal.CurrencyCode = "CAD";
                invoiceLineTotal.MonetaryValue = "10";
                tntRequest.InvoiceLineTotal = invoiceLineTotal;
                tntRequest.MaximumListSize = "1";

                UPSSecurity upss = new UPSSecurity();
                UPSSecurityServiceAccessToken upsSvcToken = new UPSSecurityServiceAccessToken();
                upsSvcToken.AccessLicenseNumber = "Your access license number";
                upss.ServiceAccessToken = upsSvcToken;
                UPSSecurityUsernameToken upsSecUsrnameToken = new UPSSecurityUsernameToken();
                upsSecUsrnameToken.Username = "Your username";
                upsSecUsrnameToken.Password = "Your password";
                upss.UsernameToken = upsSecUsrnameToken;
                tntService.UPSSecurityValue = upss;

                System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12 | System.Net.SecurityProtocolType.Tls | System.Net.SecurityProtocolType.Tls11; //This line will ensure the latest security protocol for consuming the web service call.
                Console.WriteLine(tntRequest);
                TimeInTransitResponse tntResponse = tntService.ProcessTimeInTransit(tntRequest);
                Console.WriteLine("Response code: " + tntResponse.Response.ResponseStatus.Code);
                Console.WriteLine("Response description: " + tntResponse.Response.ResponseStatus.Description);

            }
            catch (System.Web.Services.Protocols.SoapException ex)
            {
                Console.WriteLine("");
                Console.WriteLine("---------Time In Transit Web Service returns error----------------");
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
            catch (System.ServiceModel.CommunicationException ex)
            {
                Console.WriteLine("");
                Console.WriteLine("--------------------");
                Console.WriteLine("CommunicationException= " + ex.Message);
                Console.WriteLine("CommunicationException-StackTrace= " + ex.StackTrace);
                Console.WriteLine("-------------------------");
                Console.WriteLine("");

            }
            catch (Exception ex)
            {
                Console.WriteLine("");
                Console.WriteLine("-------------------------");
                Console.WriteLine(" Generaal Exception= " + ex.Message);
                Console.WriteLine(" Generaal Exception-StackTrace= " + ex.StackTrace);
                Console.WriteLine("-------------------------");

            }
            finally
            {
                Console.ReadKey();
            }
        }
    }
}