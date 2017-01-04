package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import Beans.Transport;
import adapters.CardAdapter;
import utils.Connectivity;
import utils.Links;

public class Listing_CardView_activity extends Activity  {
    private PullToRefreshRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ProgressView progressview;
    private HashMap<Integer,String> mapgoods= new HashMap<>();
    private ArrayList<Transport> listtransport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_activity);

        progressview = (ProgressView) findViewById(R.id.progress_view);
        mRecyclerView = (PullToRefreshRecyclerView ) findViewById(R.id.my_recycler_view);
        if (Connectivity.Checkinternet(this)) {
            getData(Listing_CardView_activity.this);
            ConfigureRecycleView();
        }
    }

    private void ConfigureRecycleView() {
                mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        // set true to open swipe(pull to refresh, default is true)
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setSwipeEnable(true);
        mRecyclerView.setNestedScrollingEnabled(true);
//                mRecyclerView.removeHeader();
        //setpaginablelistener
        mRecyclerView.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                getData(Listing_CardView_activity.this);
            }
        });
        // set OnRefreshListener
                mRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        getData(Listing_CardView_activity.this);
                    }
                });

        // set loadmore String
                mRecyclerView.setLoadmoreString("loading");

        // set loadmore enable, onFinishLoading(can load more? , select before item)
                mRecyclerView.onFinishLoading(true, false);

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

    public void showData(){
        mAdapter = new CardAdapter(listtransport,mapgoods,mRecyclerView,this,Listing_CardView_activity.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshComplete();
    }
    public void setGoodtypes(HashMap map)
    {
        mapgoods=map;
    }

    private void parseJSON(String json){
        ArrayList<Transport> resultlist;
        if (!json.equals(""))
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("transport");

            resultlist = new ArrayList<>();
            Transport transport;
            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                transport=new Transport(j,1);
                resultlist.add(transport);
            }
            listtransport=resultlist;
            gettypegoods task=new gettypegoods(this,this);
            task.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int gettransportID(JSONObject j){
        int id  =0;
        try {
            id = j.getInt("transport_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
    private int getgoodsid(JSONObject j){
        int result=0;
        try {
            result = j.getInt("id_type_goods");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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
    private String getUser(JSONObject j){
        String url = null;
        try {
            url = j.getString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
    private String getDateadd(JSONObject j){
        String url = null;
        try {
            url = j.getString("transport_date_add");
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



//    private User getUser(Activity activity, final int id){
//        class getUser extends AsyncTask<Void,Void,String> {
//            Activity activity;
//            @Override
//            protected void onPreExecute() {
//                progressview.start();
//                super.onPreExecute();
//            }
//            public getUser(Activity act)
//            {
//            }
//            public getUser(){}
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                progressview.stop();
//                return User(
//                try{new JSONObject(s);}catch(Exception ex){
//                    PrintStream});
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                BufferedReader bufferedReader = null;
//                try {
//                    URL url = new URL(Links.getRootFolder()+"getuser.php?user='"+String.valueOf(id)+"'");
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json = bufferedReader.readLine())!= null){
//                        sb.append(json+"\n");
//                    }
//
//                    return sb.toString().trim();
//
//                }catch(Exception e){
//                    return null;
//                }
//            }
//        }
//        getUser gd = new getUser(activity);
//        gd.execute();
//    }
}
