package tn.android.etransport.etransport;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Beans.User;
import dmax.dialog.SpotsDialog;
import utils.UserInfos;

public class UpdateUserTask extends AsyncTask<String, String, String> {

	private Context context;
	private String responseBody;
	private AlertDialog progdialog;
	private Activity parentactivity;
	private String UserString;
	private JSONArray UserJsonArray;
	private JSONObject UserJsonObject;
	private User user;

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

	public UpdateUserTask(Activity activity, Context context)
	{
		setParentactivity(activity);
		progdialog = new SpotsDialog(activity,R.style.CustomSpotDialog);
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
	            nameValuePair.add(new BasicNameValuePair("user_f_name", params[2]));
	            nameValuePair.add(new BasicNameValuePair("user_l_name",params[3]));
	            nameValuePair.add(new BasicNameValuePair("user_phone",params[4]));
	            nameValuePair.add(new BasicNameValuePair("address",params[5]));

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
			{
				UserString = json.getString("user").toString();
				UserJsonArray = json.getJSONArray("user");
				UserJsonObject = UserJsonArray.getJSONObject(0);
				if (UserJsonArray != null) {
					user = new User(UserJsonObject);
					UserInfos.setConnecteduser(null);
					UserInfos.setConnecteduser(user);
					UserInfos.IsConnected = true;
					Toast.makeText(context,"profil mis à jour", Toast.LENGTH_LONG).show();
					reload();
				}

			}
			else
			if(json.getString("error").equals("501"))
				Toast.makeText(context,"Request Error",Toast.LENGTH_LONG).show();
			//  String id=json.getString("client_id").toString();
			else
			if(json.getString("error").equals("-1"))
				Toast.makeText(context,"Connexion problem",Toast.LENGTH_LONG).show();
		}
		catch (JSONException e)        {}

	}
	private void reload()
	{
		Fragment currentFragment = parentactivity.getFragmentManager().findFragmentById(R.id.fragment_affreteur);
		FragmentTransaction fragTransaction =   (parentactivity).getFragmentManager().beginTransaction();
		fragTransaction.detach(currentFragment);
		fragTransaction.attach(currentFragment);
		fragTransaction.commit();
	}
	
	
}
