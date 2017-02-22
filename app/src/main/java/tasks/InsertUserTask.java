package tasks;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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

import dmax.dialog.SpotsDialog;
import tn.android.etransport.etransport.R;
import utils.AlertDialogCustom;

public class InsertUserTask extends AsyncTask<String, String, String> {

	Context context;
	String responseBody;
	AlertDialog progdialog;
	public Context getCntx() {
		return context;
	}

	public void setCntx(Context context) {
		this.context = context;
	}

	public InsertUserTask(Activity activity)
	{
		progdialog = new SpotsDialog(activity, R.style.CustomSpotDialog);
	}

	@Override
	protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	          
	            nameValuePair.add(new BasicNameValuePair("sprenom", params[1]));
	            nameValuePair.add(new BasicNameValuePair("snom", params[2]));
	            nameValuePair.add(new BasicNameValuePair("smail",params[3]));
	
	            nameValuePair.add(new BasicNameValuePair("smdp",params[4]));
	            nameValuePair.add(new BasicNameValuePair("sphone",params[5]));
	            nameValuePair.add(new BasicNameValuePair("status",params[6]));
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
				AlertDialogCustom.show(context,context.getResources().getString(R.string.confirm_inscription));
			else
			if(json.getString("error").equals("2"))
				AlertDialogCustom.show(context,"Mail déja existant");
			else
			if(json.getString("error").equals("-1"))
				AlertDialogCustom.show(context,"problème de connexion ");
		}
		catch (JSONException e)        {}

	}
	
	
}
