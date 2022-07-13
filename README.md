# SocGen SmartCredit (ePOS) integration guide

You can simply integrate the SmartCredit journey in your merchant website using APIs [see API documentation](https://app.swaggerhub.com/apis-docs/SG.BaaS/smart-credit_epos_e_merchant_customer_application/1.1.0)  and scripts samples [see scripts samples](samples-launching-scripts/)
You can doawnload our API specifications [there](/API-doc).

The SmartCredit journey consist of:

  1. Customer selects the SmartCredit payment method within checkout process on merchant website.
     - Some script sample is provided on how selecting the right payment method. For instance, prerequisite is that the place of residence of the applicant is in the specific country of the financing service provider. Scripts are customizable according your needs.
  4. Customer is redirecting to the SmartCredit journey. According the payment method selected it will be either a fiancing credit (German resident cutsomers) or a split payment journey (French resident customers).
     - Use API /access_token to get your access token (see Authentication chapter below)
     - Use API [/api/v1/customerApplications/do-initialize](https://app.swaggerhub.com/apis-docs/SG.BaaS/smart-credit_epos_e_merchant_customer_application/1.1.0#/Customer%20Application/newApplication) to get redirect url or page
  5. Customer has to fill in the application form and submits it for an instant approval.
  6. In case of a credit journey after **PRE-ACCEPTED** approval, the applicant is re-directed to our KYC partner Web ID to do a legitimation and an e-signing of the contract. in case **KYC-SUCCESSFUL**, financing demand will be finalized on Lenders side. 
  8. On nominal status, the application demand (credit or payment) is **ACCEPTED**. Merchant can dispatch the goods.
     - Use API [/api/v1/customerApplications/do-search](https://app.swaggerhub.com/apis-docs/SG.BaaS/smart-credit_epos_e_merchant_customer_application/1.1.0#/Customer%20Application/getApplications) to get status of a customer demand or consult your [merchant portal](https://app-portal-socgenepos-prod.ondisplayftos.com/Main#/page/home/index) 
  9. Other possible statuses are:
     - **REJECTED**: application demand is rejected. Customer is redirected to the merchant website to select another payment method.
     - **ABORTED**: application demand encountered some problem. It could be technical, time-out or any other problems.
  10. **CANCELLED or REIMBURSED** post-checkout actions are enabled in the merchant portal.  After processing customer demands statuses will be updated accordingly.

## SMARTCREDIT SEQUENCE DIAGRAM:

![This is an image](/documentation/EPOS_flow_technical_guidelines.png)

## SMARTCREDIT status life cycle
![This is an image](/documentation/Archi/ePOs%20status%20life%20cycle.jpg)
 
# Authentication

API are secured with [https://sso.sgmarkets.com/sgconnect](https://sso.sgmarkets.com/sgconnect) authentication server using oAuth2 credit credential flow. 
 
An access token is required. A POST to SG CONNECT /access_token endpoint is necessary using your client Id and secret (See Merchant onboarding process). It is conformed to RFC 6749 specifications.
  
  **POST /sgconnect/oauth2/access_token**
  >This endpoint is described in OAuth2 standard to be used to retrieve an access token (except for implicit flow).
  It generates an Access Token (OAuth) for the asked scopes (only the authorized ones) using client id/secret.
  
  1. Request Headers
     - Authorization – Basic with clientId:clientSecret encoded in base64 or mTLS with the your certificate, see the examples below.
     - Content-Type – application/x-www-form-urlencoded
  2. Form Parameters
     - string grant_type – Type of flow used
     - string scope – List of scopes asked (separated by spaces)
  3. Response Headers
     - Content-Type – application/json
  4. Response JSON Object
     - access_token (string) – Access token
     - id_token (string) – ID token
     - scope (string) – List of scopes for which the token is valid for
     - token_type (string) – Must be Bearer
     - expires_in (int) – TTL of the access token
  5. Status Codes
     - 200 OK – Request accepted, token provided
     - 400 Bad Request – Mainly for invalid clients
     - 401 Unauthorized – Bad credentials, please verify your client credentials

Example request:
~~~
POST /sgconnect/oauth2/access_token?grant_type=client_credentials&scope=openid api.epos-emerchant-initiate-customer-application.v1 HTTP/1.1
Host: sso.sgmarkets.com
Content-Type: application/x-www-form-urlencoded
Authorization: Basic Nzk3ZDI0ZjQtNmZ...
~~~
Example Response:
~~~
  HTTP/1.1 200 OK
  Content-Type: application/json
    {
    "access_token":"eyJ0eXAiOiJKV1QiLCJraWQiOiJaVEJ0N3ZxRVlTV056UHlkd0...",
    "scope":"sgx@@origin_network@@Internet openid api.epos-emerchant-initiate-customer-application.v1",
    "token_type":"Bearer",
    "expires_in":599
    }
~~~
Samples of codes are available in different languages.
> IMPORTANT: 
- Call of API must be done on server side. 
- Don’t store this API Key and secret on client side
- Don’t store directly in your code
- Don’t expose unencrypted credentials on code repositories, even private ones
- Consider using an API secret management service (in a key vault for instance)

# Webhooks
As of today, no webhooks are available yet, but this is part of backlog. It will allow to subscribe to event such status changes post-checkout events.

# Plugins
As of today, no CMS plugins (Magento, Shopify …) are available yet, but this is part of backlog. 

# Give us your feedback
We are pleased to receive your feedback on this documentation or any suggestion you may have. Feel free to use our [Slack](https://slack.com) Socgen-BaaS-SmartCredit integration  channel (connect to slack and enter "socgenbaassma-hcq2071.com" to join our channel).  
