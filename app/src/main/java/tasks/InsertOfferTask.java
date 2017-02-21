package tasks;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Beans.Transport;
import dmax.dialog.SpotsDialog;
import tn.android.etransport.etransport.R;
import utils.Links;
import utils.UserInfos;

public class InsertOfferTask extends AsyncTask<String, String, String> {

	Context context;
	String responseBody;
	AlertDialog progdialog;
	private Transport transport;
	private String price;
	public Context getCntx() {
		return context;
	}

	public void setCntx(Context context, Transport transport,String price) {
		this.context = context;
		this.transport=transport;
		this.price=price;
	}

	public InsertOfferTask(Activity activity)
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
	          
	            nameValuePair.add(new BasicNameValuePair("price", params[1]));
	            nameValuePair.add(new BasicNameValuePair("id_transport", params[2]));
	            nameValuePair.add(new BasicNameValuePair("id_transporter",params[3]));
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
			{	Toast.makeText(context,"Votre demande est envoyée",Toast.LENGTH_LONG).show();
			//notify the shipper
			SendNotificationFromTransporter notification= new SendNotificationFromTransporter(context);
			notification.execute(Links.getRootFolder()+"sendPushNotification.php","Proposition Reçu"
					,"le transporteur "+ UserInfos.getConnecteduser().getF_name()+UserInfos.getConnecteduser().getL_name()
							+" a proposé "+price +" pour votre trajet de "+transport.getAddress_from()+" à "+transport.getAddress_to()
					,String.valueOf(transport.getUser_id()));}
			else
			if(json.getString("error").equals("2"))
				Toast.makeText(context,"vous avez déja proposer votre offre pour ce trajet",Toast.LENGTH_LONG).show();
			//  String id=json.getString("client_id").toString();
			else
			if(json.getString("error").equals("-1"))
				Toast.makeText(context,"Connexion problem",Toast.LENGTH_LONG).show();
		}
		catch (JSONException e)        {}

	}
	
	
}
