package tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import tn.android.etransport.etransport.Listing_CardView_activity;
import tn.android.etransport.etransport.R;
import utils.Links;

/**
 * Created by mohamed salah on 24/12/2016.
 */

public class gettypegoods extends AsyncTask<String,String,String> {

    private Context context;
    private AlertDialog progdialog;
    private Listing_CardView_activity mainActivity;
    private HashMap<Integer,String> mapgoods= new HashMap<>();


    public gettypegoods(Context context, Listing_CardView_activity mainActivity){
        this.context = context;
        this.mainActivity = mainActivity;
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
        //TODO parse json type goods
        //parse to Json
        try {
            JSONObject jsonObject = new JSONObject(Result);
            JSONArray array = jsonObject.getJSONArray("goods");


            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                mapgoods.put(j.getInt("type_goods_id"),j.getString("goods_desc"));
            }
            mainActivity.setGoodtypes(mapgoods);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mainActivity.showData();
        progdialog.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(Links.getRootFolder()+"gettypegoods.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            return sb.toString().trim();

        }catch(Exception e){
            return null;
        }

    }


}
