package tn.android.etransport.etransport;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class InsertTransportTask extends AsyncTask<String, String, String> {

	private Context context;
	private String responseBody;
	private ProgressDialog progdialog;
	private Activity parentactivity;

	public Activity getParentactivity() {
		return parentactivity;
	}

	public void setParentactivity(Activity parentactivity) {
		this.parentactivity = parentactivity;
	}

	public Context getCntx() {
		return context;
	}

	public void setCntx(Context context) {
		this.context = context;
	}

	public InsertTransportTask(Activity activity,Context context)
	{
		progdialog = new ProgressDialog(activity,R.style.NewDialog);
		this.context=context;
	}

	@Override
	protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	          
	            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
	            nameValuePair.add(new BasicNameValuePair("date_go", params[2]));
	            nameValuePair.add(new BasicNameValuePair("date_arrival",params[3]));
	            nameValuePair.add(new BasicNameValuePair("text",params[4]));
	            nameValuePair.add(new BasicNameValuePair("city_from",params[5]));
	            nameValuePair.add(new BasicNameValuePair("city_to",params[6]));
				nameValuePair.add(new BasicNameValuePair("country_from",params[7]));
				nameValuePair.add(new BasicNameValuePair("country_to",params[8]));
				nameValuePair.add(new BasicNameValuePair("address_from",params[9]));
				nameValuePair.add(new BasicNameValuePair("address_to",params[10]));

	            //Encode and set entity
	            post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
	            HttpResponse response = client.execute(post);
				responseBody = EntityUtils.toString(response.getEntity());

        	}
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        catch (Exception e) {
	        }

        return responseBody;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progdialog.setMessage("changement en cours");
		progdialog.show();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progdialog.dismiss();
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			if(json.getString("error").equals("0"))
			{
				Toast.makeText(context,"Insertion Done",Toast.LENGTH_LONG).show();
				Intent intent = new Intent(parentactivity,MainClient.class);
				parentactivity.startActivity(intent);
				parentactivity.finish();
			}
			else
			if(json.getString("error").equals("501"))
				Toast.makeText(context,"Request Error",Toast.LENGTH_LONG).show();
			//  String id=json.getString("client_id").toString();
			else
			if(json.getString("error").equals("-1"))
				Toast.makeText(context,"Connexion problem",Toast.LENGTH_LONG).show();
			else
			if(json.get("error").equals("3"))
				Toast.makeText(context,"error while adding the city",Toast.LENGTH_LONG).show();
		}
		catch (JSONException e)        {}

	}
	
	
}
