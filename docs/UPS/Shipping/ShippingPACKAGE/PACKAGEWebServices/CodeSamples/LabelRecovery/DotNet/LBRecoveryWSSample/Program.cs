using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using LabelRecoveryExample.LabelRecoveryWebReference;


namespace LabelRecoveryExample
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                LBRecovery lb_recovery = new LBRecovery();
                LabelRecoveryRequest label_recovery_request = new LabelRecoveryRequest();
                UPSSecurity upss = new UPSSecurity();
                UPSSecurityServiceAccessToken upssSvcAccessToken = new UPSSecurityServiceAccessToken();
                upssSvcAccessToken.AccessLicenseNumber = "Your access license number";
                
                upss.ServiceAccessToken = upssSvcAccessToken;
                UPSSecurityUsernameToken upssUsrNameToken = new UPSSecurityUsernameToken();
                
                upssUsrNameToken.Username = "Your username";
                upssUsrNameToken.Password = "Your password";

                upss.UsernameToken = upssUsrNameToken;
                lb_recovery.UPSSecurityValue = upss;
                RequestType request = new RequestType();
                label_recovery_request.Request = request;
                
                label_recovery_request.TrackingNumber = "Your tracking number";

                System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12 | System.Net.SecurityProtocolType.Tls | System.Net.SecurityProtocolType.Tls11; //This line will ensure the latest security protocol for consuming the web service call.
                LabelRecoveryResponse label_recovery_response = lb_recovery.ProcessLabelRecovery(label_recovery_request);
                LabelImageType image=(LabelImageType)label_recovery_response.Items[0];
                Console.WriteLine("Image Base64:\n " + image.GraphicImage);
                Console.ReadKey();
            }
            catch (System.Web.Services.Protocols.SoapException ex)
            {
                Console.WriteLine("");
                Console.WriteLine("---------LBRecovery Web Service returns error----------------");
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
            } catch (Exception ex)
            {
                Console.WriteLine("");
                Console.WriteLine("-------------------------");
                Console.WriteLine(" General Exception= " + ex.Message);
                Console.WriteLine(" General Exception-StackTrace= " + ex.StackTrace);
                Console.WriteLine("-------------------------");

            }
            finally
            {
                Console.ReadKey();
            }

        }
    }
}
