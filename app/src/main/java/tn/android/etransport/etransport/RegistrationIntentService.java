package tn.android.etransport.etransport;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import utils.Links;
import utils.UserInfos;

/**
 * Created by mohamed salah on 13/02/2017.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }
    String token;
    String uid;
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {

            TelephonyManager tManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            uid = tManager.getDeviceId();
            Log.e("serial number", uid);
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]

            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        Upload u=new Upload(this);
        u.execute(Links.getRootFolder()+"notifications_services.php"
                ,String.valueOf(UserInfos.getConnecteduser().getId()),token,uid);
    }

    private 	class Upload extends AsyncTask<String,String,String>
    {


        private SharedPreferences mSettings;
        String responseBody;
        Upload(Context c) {


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


        }


        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            if(s== 0)
            {
                Log.e("token send","success");
            }
            else

            {
                Log.e("token send","failure");
            }
        }



        String url= Links.getRootFolder()+"notifications_services.php";
        int s;
        @Override
        protected String doInBackground(String... params) {
            mSettings	= getApplicationContext().getSharedPreferences("account", 0);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

            try {
                // set up post data
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                nameValuePair.add(new BasicNameValuePair("iduser", params[1]));
                nameValuePair.add(new BasicNameValuePair("token", params[2]));
                nameValuePair.add(new BasicNameValuePair("serial", params[3]));
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

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}