package tasks;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 24/12/2016.
 */

public class SendNotificationFromTransporter extends AsyncTask<String,String,String> {

    private Context context;
    private AlertDialog progdialog;
    private String responseBody;

    public SendNotificationFromTransporter(Context context){
        this.context = context;
        progdialog = new SpotsDialog(context, R.style.CustomSpotDialog);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progdialog.show();
    }

    @Override
    protected void onPostExecute(String Result) {
        super.onPostExecute(Result);
        //parse to Json
//        try {
//            JSONObject jsonObject = new JSONObject(Result);
//            JSONArray array = jsonObject.getJSONArray("message");
//            Log.d("message",array.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        progdialog.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

        try {
            // set up post data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("title", params[1]));
            nameValuePair.add(new BasicNameValuePair("message", params[2]));
            nameValuePair.add(new BasicNameValuePair("iduser",params[3]));
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


}
