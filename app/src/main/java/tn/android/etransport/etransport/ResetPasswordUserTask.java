package tn.android.etransport.etransport;

/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
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

import dmax.dialog.SpotsDialog;

public class ResetPasswordUserTask extends AsyncTask<String, String, String> {

	Context context;
	String responseBody;
	AlertDialog progdialog;
	public Context getCntx() {
		return context;
	}

	public void setCntx(Context context) {
		this.context = context;
	}
	public ResetPasswordUserTask(Activity act)
	{
		progdialog = new SpotsDialog(act,R.style.CustomSpotDialog);
	}
	@Override
	protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	          
	            nameValuePair.add(new BasicNameValuePair("mail", params[1]));
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
		progdialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progdialog.dismiss();
		JSONObject json = null;
		if (!result.equals(""))
		try {
			json = new JSONObject(result);
			if(json.getString("error").equals("0"))
				Toast.makeText(context,"Modification effecutée",Toast.LENGTH_LONG).show();
			else
			if(json.getString("error").equals("-1"))
				Toast.makeText(context,"Connexion problem",Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context,"Mail non existant",Toast.LENGTH_LONG).show();

		}
		catch (JSONException e)        {}

	}
	
	
}
