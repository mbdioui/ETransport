package tasks;
/**
 * Created by mohamed salah
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import net.steamcrafted.loadtoast.LoadToast;

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
import tn.android.etransport.etransport.Home_activity;
import tn.android.etransport.etransport.R;

public class InsertTransportTask extends AsyncTask<String, String, String> {

	private Context context;
	private String responseBody;
	private AlertDialog progdialog;
	private Activity parentactivity;
	private LoadToast lt;

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
		progdialog = new SpotsDialog(activity, R.style.CustomSpotDialog);
		this.context=context;
	}

	@Override
	protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	          /*  $user_id = isset($_POST['user_id']) ? $_POST['user_id'] : '';
					$date_go_ini=isset($_POST['date_go']) ? $_POST['date_go'] :'';
					$date_arrival_ini=isset($_POST['date_arrival']) ? $_POST['date_arrival'] :'';
					$date_go_min_ini=isset($_POST['date_go_min']) ? $_POST['date_go_min'] :'';
					$date_go_max_ini=isset($_POST['date_go_max']) ? $_POST['date_go_max'] :'';
					$date_arrival_min_ini=isset($_POST['date_arrival_min']) ? $_POST['date_arrival_min'] :'';
					$date_arrival_max_ini=isset($_POST['date_arrival_max']) ? $_POST['date_arrival_max'] :'';
					$text=isset($_POST['text']) ? addslashes($_POST['text']) :'';
					$address_from=isset($_POST['address_from']) ? $_POST['address_from'] :'';
					$address_to=isset($_POST['address_to']) ? $_POST['address_to'] :'';
					$type_pub=isset($_POST['type_pub']) ? $_POST['type_pub'] : '1';
					$start_pos_lat=isset($_POST['start_pos_lat']) ? $_POST['start_pos_lat'] :'';
					$start_pos_lgt=isset($_POST['start_pos_lgt']) ? $_POST['start_pos_lgt'] :'';
					$end_pos_lat=isset($_POST['end_pos_lat']) ? $_POST['end_pos_lat'] :'';
					$end_pos_lgt=isset($_POST['end_pos_lgt']) ? $_POST['end_pos_lgt'] :'';*/

	            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
	            nameValuePair.add(new BasicNameValuePair("date_go_min", params[2]));
	            nameValuePair.add(new BasicNameValuePair("date_go_max",params[3]));
	            nameValuePair.add(new BasicNameValuePair("date_arrival_min",params[4]));
	            nameValuePair.add(new BasicNameValuePair("date_arrival_max",params[5]));
	            nameValuePair.add(new BasicNameValuePair("text",params[6]));
				nameValuePair.add(new BasicNameValuePair("address_from",params[7]));
				nameValuePair.add(new BasicNameValuePair("address_to",params[8]));
				nameValuePair.add(new BasicNameValuePair("start_pos_lat",params[9]));
				nameValuePair.add(new BasicNameValuePair("start_pos_lgt",params[10]));
				nameValuePair.add(new BasicNameValuePair("end_pos_lat",params[11]));
				nameValuePair.add(new BasicNameValuePair("end_pos_lgt",params[12]));
				nameValuePair.add(new BasicNameValuePair("date_go",params[13]));
				nameValuePair.add(new BasicNameValuePair("date_arrival",params[14]));
				nameValuePair.add(new BasicNameValuePair("type_pub",params[15]));
				nameValuePair.add(new BasicNameValuePair("means",params[16]));
				nameValuePair.add(new BasicNameValuePair("typegood",params[17]));
				nameValuePair.add(new BasicNameValuePair("picture1",params[18]));
				nameValuePair.add(new BasicNameValuePair("picture2",params[19]));
				nameValuePair.add(new BasicNameValuePair("picture3",params[20]));
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
		lt= new LoadToast(context);
		lt.setText("Enregistrement en cours...");
		lt.show();

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
//				Toast.makeText(context,"Insertion Done",Toast.LENGTH_LONG).show();
				lt.success();
				Intent intent = new Intent(parentactivity,Home_activity.class);
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
