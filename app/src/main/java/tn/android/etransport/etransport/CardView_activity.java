package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Beans.Transport;
import adapters.CardAdapter;
import utils.Links;

public class CardView_activity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ProgressView progressview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_activity);

        progressview = (ProgressView) findViewById(R.id.progress_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getData(CardView_activity.this);
    }

    private void getData(Activity activity){
        class GetData extends AsyncTask<Void,Void,String> {
            Activity activity;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            public GetData(Activity act)
            {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressview.stop();
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(Links.getRootFolder()+"gettransport.php");
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
        GetData gd = new GetData(activity);
        gd.execute();
    }

    public void showData(List<Transport> list){
        mAdapter = new CardAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void parseJSON(String json){
        ArrayList<Transport> resultlist;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("transport");

            resultlist = new ArrayList<>();
            Transport transport;
            for(int i=0; i<array.length()-1; i++){
                transport=new Transport();
                JSONObject j = array.getJSONObject(i);
                transport.setTransport_id(Integer.parseInt(getID(j)));
                transport.setText(getDescription(j));
                resultlist.add(transport);
            }
            showData(resultlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getID(JSONObject j){
        String name = null;
        try {
            name = j.getString("transport_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getDescription(JSONObject j){
        String url = null;
        try {
            url = j.getString("transport_text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent(getApplication(),Home_affreteur_activity.class);
        startActivity(intent);
    }
}
