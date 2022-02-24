package socgen.epos;

import java.io.IOException;
import java.net.URL;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class Main {
	
	public static void main(String[] args) {
		launchLoanOrigination();
		try {
			Thread.sleep(300000);//300 seconds
			launchLoanOrigination();//use the same token
			Thread.sleep(300000);//600 seconds Token should be expired
			launchLoanOrigination();//request new token
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void launchLoanOrigination() {
		//application: json object to be sent to Epos for loan request
				String application = "{\r\n"
						+ "    \"amount\": 114,\r\n"
						+ "    \"customerId\": \"701943\",\r\n"
						+ "    \"normalReturnUrl\": \"http://merchanttesting.merchanttestingmerchant.com\",\r\n"
						+ "     \"orderId\": \"PM_050b109\",\r\n"
						+ "    \"paymentMean\": \"REVOLVINGPOS\",\r\n"
						+ "    \"shoppingCartDetail\": {\r\n"
						+ "        \"mainProduct\": \"PANTALON B MAGO\",\r\n"
						+ "        \"shoppingCartTotalAmount\": \"95\",\r\n"
						+ "        \"shoppingCartTotalQuantity\": \"2\",\r\n"
						+ "        \"SKU\": \"14\",\r\n"
						+ "        \"shoppingCartItemList\": {\r\n"
						+ "            \"productName\": \"PANTALON B MAGO\",\r\n"
						+ "            \"productUnitAmount\": \"47.50\",\r\n"
						+ "            \"productQuantity\": \"2\",\r\n"
						+ "            \"productDescription\": \"desc\"\r\n"
						+ "        }\r\n"
						+ "    },\r\n"
						+ "    \"customerAddress\": {\r\n"
						+ "        \"city\": \"GAP\",\r\n"
						+ "        \"street\": \"10\",\r\n"
						+ "        \"zipCode\": \"75000\",\r\n"
						+ "        \"country\": \"FR\"\r\n"
						+ "    },\r\n"
						+ "    \"billingAddress\": {\r\n"
						+ "        \"city\": \"GAP\",\r\n"
						+ "        \"street\": \"voltaire\",\r\n"
						+ "        \"zipCode\": \"75000\",\r\n"
						+ "        \"country\": \"FR\",\r\n"
						+ "        \"company\": \"DHL\"\r\n"
						+ "    },\r\n"
						+ "    \"deliveryAddress\": {\r\n"
						+ "        \"city\": \"GAP\",\r\n"
						+ "        \"street\": \"voltaire\",\r\n"
						+ "        \"zipCode\": \"75000\",\r\n"
						+ "        \"country\": \"FR\",\r\n"
						+ "        \"company\": \"DHL\"\r\n"
						+ "    },\r\n"
						+ "    \"customerContact\": {\r\n"
						+ "        \"legalId\": \"\",\r\n"
						+ "        \"email\": \"meriem.loukili@tesselate.com\",\r\n"
						+ "        \"lastName\": \"loukili\",\r\n"
						+ "        \"firstName\": \"meriem\",\r\n"
						+ "        \"mobile\": \"+33234749393\",\r\n"
						+ "        \"phone\": \"+49234749393\"\r\n"
						+ "    },\r\n"
						+ "    \"billingContact\": {\r\n"
						+ "        \"legalId\": \"\",\r\n"
						+ "        \"email\": \"test@test.com\",\r\n"
						+ "        \"lastName\": \"test\",\r\n"
						+ "        \"firstName\": \"test\",\r\n"
						+ "        \"mobile\": \"0334749393\",\r\n"
						+ "        \"phone\": \"0334749393\"\r\n"
						+ "    },\r\n"
						+ "    \"deliveryContact\": {\r\n"
						+ "        \"email\": \"test@test.com\",\r\n"
						+ "        \"lastName\": \"test\",\r\n"
						+ "        \"firstName\": \"test\",\r\n"
						+ "        \"mobile\": \"0334749393\",\r\n"
						+ "        \"phone\": \"0334749393\"\r\n"
						+ "    }\r\n"
						+ "   \r\n"
						+ "}";
				
				
				try {
					
					EposInterface eposInterface = new EposInterface();
					Response response = eposInterface.sendLoanApplication(application);
					ResponseBody bodyresponse = response.body();
					String answer = bodyresponse.string();
					
					if(response.code() == 200) {//application is successful 
						if(isURL(answer)) {
							//redirect your customer to this URL
							System.out.println("HB journey");
						}else {
							//html response (FF journey) redirect your customer to this page
							System.out.println("FF journey");
						}
					}else {
					 //an error has occurred, handle error
						System.out.println("An error has occured");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	}
	
	
	public static  boolean isURL(String url) {
	    try {
	        new URL(url);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}
