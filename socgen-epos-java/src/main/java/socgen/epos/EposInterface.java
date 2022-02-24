package socgen.epos;

import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class EposInterface {
	
	private String eposURL = "epos_URL"+"/ProcessAndRedirect";

	Response sendLoanApplication(String applicationJson) {
		
		LazyTokenGeneratorSingleton lazyTokenGeneratorSingleton = LazyTokenGeneratorSingleton.getInstance();
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, applicationJson);
		
		Request request = new Request.Builder().url(eposURL + "?access_token="+ lazyTokenGeneratorSingleton.getSGConnectToken()).post(body).build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	

}
