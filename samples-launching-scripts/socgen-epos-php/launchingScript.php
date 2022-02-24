<?php

    $token;
    $expires_in;
    $dateRequest;
	$login = "your_login";
	$password = "your_password";
	$eposURL = "epos_url";
	$SG_URL = "sg_connect_url";

    function retreiveToken()
    {
        $GLOBALS['dateRequest'] = new DateTime();
        $tokenCurl = curl_init();

        curl_setopt_array($tokenCurl, array(
            CURLOPT_URL => $GLOBALS['SG_URL'],
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "POST",
            CURLOPT_HTTPHEADER => array(
                "accept: */*",
                "authorization: Basic ".getAuthorization(),
                "cache-control: no-cache",
                "content-type: application/x-www-form-urlencoded"
            ) ,
        ));

        $tokenResponse = curl_exec($tokenCurl);
        $tokenErr = curl_error($tokenCurl);

        curl_close($tokenCurl);

        if ($tokenErr)
        {
            echo "cURL Error #:" . $tokenErr;
            exit;
        }
        $GLOBALS['token'] = json_decode($tokenResponse)-> access_token;
        $GLOBALS['expires_in'] = json_decode($tokenResponse)-> expires_in;
        //return $tokenResponse;
        
    }

    function sendRequest($request)
    {
        $curl = curl_init();

        curl_setopt_array($curl, array(
            CURLOPT_URL => $GLOBALS['eposURL'] . getToken() ,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "POST",
            CURLOPT_POSTFIELDS => $request,
            CURLOPT_HTTPHEADER => array(
                "cache-control: no-cache"
            ) ,
        ));

        $response = curl_exec($curl);
        $err = curl_error($curl);

        curl_close($curl);

        if ($err)
        {
            echo "cURL Error #:" . $err;
        }
        else
        {
            //echo $response;
			if(strpos($response, 'auxiliaryDataId'))
			{//HB flow
				//header("Location: ".$response);
				//die();
				echo $response;
			}else
			{
				echo $response;//FF flow
			}     
        }
    }

    function getToken()
    {
		
        if (isFirstRequest() || isExpired())
        {
		retreiveToken();
        }
        return $GLOBALS['token'];
    }

    function isFirstRequest()
    {
		global $dateRequest;
        return $GLOBALS['dateRequest'] == null;
    }

    function isExpired()
    {
        $newDate = new DateTime();
            $GLOBALS['dateRequest']->add(new DateInterval('PT' . $GLOBALS['expires_in']));
            $interval = $GLOBALS['dateRequest']->diff($newDate);
        if ($interval > 0) return true;
        else return false;
    }
	
	function getAuthorization() 
	{
		$auth = $GLOBALS['login'] . ":" . $GLOBALS['password'];
		return base64_encode($auth);
}
    echo sendRequest("{\r\n    \"amount\": 1111,\r\n    \"customerId\": \"701943\",\r\n    \"normalReturnUrl\": \"https://hhhh/\",\r\n    \"shoppingCartDetail\": {\r\n        \"mainProduct\": \"PANTALON B MAGO\",\r\n        \"shoppingCartTotalAmount\": \"1500\",\r\n        \"shoppingCartTotalQuantity\": \"2\",\r\n        \"SKU\": \"50\",\r\n        \"shoppingCartItemList\": {\r\n            \"productName\": \"PANTALON B MAGO\",\r\n            \"productUnitAmount\": \"1500\",\r\n            \"productQuantity\": \"2\",\r\n            \"productDescription\": \"desc\"\r\n        }\r\n    },\r\n    \"customerAddress\": {\r\n        \"city\": \"Paris1\",\r\n        \"street\": \"voltaire\",\r\n        \"zipCode\": \"75000\",\r\n        \"country\": \"FR\"\r\n    },\r\n    \"billingAddress\": {\r\n        \"city\": \"Paris2\",\r\n        \"street\": \"voltaire\",\r\n        \"zipCode\": \"75000\",\r\n        \"country\": \"FR\",\r\n        \"company\": \"\"\r\n    },\r\n    \"deliveryAddress\": {\r\n        \"city\": \"Paris3\",\r\n        \"street\": \"voltaire\",\r\n        \"zipCode\": \"75000\",\r\n        \"country\": \"FR\",\r\n        \"company\": \"\"\r\n    },\r\n    \"customerContact\": {\r\n        \"legalId\": \"\",\r\n        \"email\": \"customer@email.com\",\r\n        \"lastName\": \"Doe\",\r\n        \"firstName\": \"John\",\r\n        \"mobile\": \"0676188860\",\r\n        \"phone\": \"0676188860\"\r\n    },\r\n    \"billingContact\": {\r\n        \"legalId\": \"\",\r\n        \"email\": \"customer@email.com\",\r\n        \"lastName\": \"bill\",\r\n        \"firstName\": \"John\",\r\n        \"mobile\": \"0676188860\",\r\n        \"phone\": \"0676188860\"\r\n    },\r\n     \"deliveryContact\": {\r\n        \"legalId\": \"\",\r\n        \"email\": \"customer@email.com\",\r\n        \"lastName\": \"deliv\",\r\n        \"firstName\": \"John\",\r\n        \"mobile\": \"0676188860\",\r\n        \"phone\": \"0676188860\"\r\n    },\r\n    \"orderId\": \"ORD366FRF_3X\",\r\n    \"merchantId\": \"797d24f4-6ff2-42c0-89f3-01a2989a0ae9\",\r\n    \"paymentMean\": \"REVOLVINGPOS\"\r\n}");
