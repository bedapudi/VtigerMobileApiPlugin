
package com.example.vtigerApi;
 
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class VtigerMApiPlugin extends CordovaPlugin {
	public static final String ACTION_SEND_HTTP_REQUEST = "sendHttpRequest";
	
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		 System.out.println("#### Java VtigerMApiPlugin execute called...");
         Log.d("gvtiger", "#### Java VtigerMApiPlugin execute called...");
        if (ACTION_SEND_HTTP_REQUEST.equals(action)) { 
		    	return sendHttpRequest(args, callbackContext);
        }
        callbackContext.error("Invalid action");
	    return false;
    }	
	private boolean sendHttpRequest(JSONArray args,  CallbackContext callbackContext){
		// do the work of Sending Http request in  //
    	
//	    JSONObject arg_object = args.getJSONObject(0);
//	    Intent calIntent = new Intent(Intent.ACTION_EDIT)
//	        .setType("vnd.android.cursor.item/event")
//	        .putExtra("beginTime", arg_object.getLong("startTimeMillis"))
//	        .putExtra("endTime", arg_object.getLong("endTimeMillis"))
//	        .putExtra("title", arg_object.getString("title"))
//	        .putExtra("description", arg_object.getString("description"))
//	        .putExtra("eventLocation", arg_object.getString("eventLocation"));
//	 
//	       this.cordova.getActivity().startActivity(calIntent);
        try{
	       System.out.println("#### Java sendHttpRequest called..");
           JSONObject arg_object = args.getJSONObject(0);
           String url = arg_object.getString("url");
           if (!url.endsWith("modules/Mobile/api.php")) {
                    url = url + "modules/Mobile/api.php";
                }
           String userName = arg_object.getString("userName");
           String password = arg_object.getString("password");
           System.out.println("#### Java sendHttpRequest called..password.."+password);
           System.out.println("#### Java sendHttpRequest called..userName.."+userName);
           System.out.println("#### Java sendHttpRequest url :"+url);
           
           HttpClient httpClient = getHttpClient(url);
           HttpPost httpPost = new HttpPost(url);
           List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
           nameValuePairs.add(new BasicNameValuePair("_operation", "loginAndFetchModules"));
           nameValuePairs.add(new BasicNameValuePair("username", userName));
           nameValuePairs.add(new BasicNameValuePair("password", password));
           HttpResponse httpResponse = httpClient.execute(httpPost);
           HttpEntity httpEntity = httpResponse.getEntity();
           getJsonNodeFromInputStream(httpEntity.getContent());
           
           callbackContext.success();
	       return true;
	} catch(Exception e) {
	    System.err.println("Exception: " + e.getMessage());
	    callbackContext.error(e.getMessage());
	    return false;
	}
	}
    
    public static void getJsonNodeFromInputStream(InputStream is) throws IOException {
        //String response = IOUtils.toString(is, "UTF-8");
        response = response.replace("\ufeff", ""); //Handing zero width no break space 
        System.out.println("#### Java HTtp response :"+response);
        
    }
    
    private HttpClient getHttpClient(String url) {

        HttpClient httpClient = new DefaultHttpClient();

        // Allow accepting self-signed certificats also
        if (url.toLowerCase().startsWith("https")) {
            int port = 443;
            Matcher portMatcher = Pattern.compile("https://[^:]+:([0-9]+)").matcher(url);
            if (portMatcher.find()) {
                port = Integer.parseInt(portMatcher.group(1));
            }

            // Enable to allow self-signed certificates
            // NOTE: Issue noticed [ E/NativeCrypto(  525): Unknown error 5 during connect ]
            //httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", new EasySSLSocketFactory(), port));

        }
        return httpClient;
    }

}
