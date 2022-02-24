package socgen.epos;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class LazyTokenGeneratorSingleton {
	
	/*this class implements the Singleton pattern
	 * Singleton pattern restricts the instantiation of a class 
	 * and ensures that only one instance of the class exists in the java virtual machine.
	 */
	
	private String token;
	private String login = "your_login";
	private String password = "your_password";
	private int expires_in;
	private Date dateRequest;
	private String SG_URL = "SG_CONNECT_URL";

    private static LazyTokenGeneratorSingleton instance;
    
    //prevent constructor's use
    private LazyTokenGeneratorSingleton(){}
    
    /*thread-safe singleton class is to make the global access method synchronized, 
     * so that only one thread can execute this method at a time.
     */
    
    public static synchronized LazyTokenGeneratorSingleton getInstance(){
        if(instance == null){
			System.out.println("new LazyTokenGeneratorSingleton");
            instance = new LazyTokenGeneratorSingleton();
        }
        return instance;
    }
	
	public String getSGConnectToken() {
		if (isFirstRequest() || isExpired()) {
			retreiveToken();
		}
		return token;
	}

	private boolean isFirstRequest() {
		return dateRequest == null;
	}

	private boolean isExpired() {
		Date date = addSeconds(dateRequest, expires_in);
		if (new Date().compareTo(date) > 0) {
			System.out.println("Token is expired");
			return true;
		}
		return false;
	}

	private void retreiveToken() {
		dateRequest = new Date();
		System.out.println("retreiveToken");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "");

		Request request = new Request.Builder().url(SG_URL).method("POST", body)
				.addHeader("content-type", "application/x-www-form-urlencoded")
				.addHeader("accept", "*/*")
				.addHeader("authorization", "Basic " + getAuthorization())
				.build();

		SGResponse json = null;
		try {
			Response response = client.newCall(request).execute();
			ResponseBody bodyresponse = response.body();
			String answer = bodyresponse.string();
			json = mapper.readValue(answer, SGResponse.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		token = json.getAccess_token();
		expires_in = json.getExpires_in();
	}

	private String getAuthorization() {
		String auth = login + ":" + password;
		return Base64.getUrlEncoder().encodeToString(auth.getBytes());
	}

	private Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

}
