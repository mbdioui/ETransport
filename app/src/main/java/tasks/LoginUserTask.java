package tasks;

import android.app.Activity;
import android.app.AlertDialog;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Beans.User;
import dmax.dialog.SpotsDialog;
import tn.android.etransport.etransport.Home_activity;
import tn.android.etransport.etransport.R;
import utils.UserInfos;

public class LoginUserTask extends AsyncTask<String, String, String> {
	private Context cntx;
	private String responseBody;
	private String Auth=null;
	private User user;
	private String  UserString;


	private AlertDialog progdialog;
	private JSONArray UserJsonArray;
	private JSONObject UserJsonObject;
	private Activity activityparent;

	public Context getCntx() {
		return cntx;
	}

	public void setCntx(Context cntx) {
		this.cntx = cntx;
	}

	public LoginUserTask(Activity act) {
		super();
		progdialog = new SpotsDialog(act, R.style.CustomSpotDialog);
		activityparent=act;
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
	progdialog.show();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progdialog.dismiss();
		JSONObject json = null;
		if(result!=null) {
			try {
				json = new JSONObject(result);
				if (json.getString("error").equals("0")) {
					UserString = json.getString("user").toString();
					UserJsonArray = json.getJSONArray("user");
					UserJsonObject = UserJsonArray.getJSONObject(0);
					user = new User(UserJsonObject);
					//TODO GETTING ROLE OF USER
					UserInfos.setConnecteduser(user);
					UserInfos.IsConnected = true;
					Toast.makeText(cntx, "Bievenue :" + user.getF_name().toString().toUpperCase(), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(activityparent, Home_activity.class);
					activityparent.startActivity(intent);
					activityparent.finish();
				} else if (json.getString("error").equals("1"))
					Toast.makeText(cntx, "Mot de passe incorrecte", Toast.LENGTH_LONG).show();
				else if (json.getString("error").equals("2"))
					Toast.makeText(cntx, "Mail non existant", Toast.LENGTH_LONG).show();
			} catch (JSONException e) {}
		}
		else
		{
			Toast.makeText(cntx, "echec de connexion,veuillez ressayer", Toast.LENGTH_LONG).show();
		}
	}


	
	
}
