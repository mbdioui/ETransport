package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoginUserTask extends AsyncTask<String, String, String> {
	Context cntx;
	String responseBody;
	String Auth=null;
	String user;
	ProgressDialog progdialog;
	public Context getCntx() {
		return cntx;
	}

	public void setCntx(Context cntx) {
		this.cntx = cntx;
	}

	public LoginUserTask(Activity act) {
		super();
		progdialog = new ProgressDialog(act);

	}

	@Override
	protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);
        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	          
	            nameValuePair.add(new BasicNameValuePair("email", params[1]));
	            nameValuePair.add(new BasicNameValuePair("mdp", params[2]));
	            //Encode and set entity
	            post.setEntity(new UrlEncodedFormEntity(nameValuePair, org.apache.http.protocol.HTTP.UTF_8));
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
	progdialog.setMessage("Authentification en cours");
	progdialog.show();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progdialog.dismiss();
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			if(json.getString("error").equals("0")) {
				Toast.makeText(cntx, "user authentified", Toast.LENGTH_LONG).show();
				user = json.getString("user").toString();
				this.Auth="ok";
			}
				else
			if(json.getString("error").equals("1"))
				Toast.makeText(cntx,"Wrong password",Toast.LENGTH_LONG).show();
			else
			if(json.getString("error").equals("2"))
				Toast.makeText(cntx,"cannot fin any account with this mail",Toast.LENGTH_LONG).show();
		}
		catch (JSONException e)        {}

	}
	
	
}
