package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.rey.material.widget.ProgressView;

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
import java.util.HashMap;

import Beans.Offer;
import adapters.OffersAdapter;
import utils.Connectivity;
import utils.Links;
import utils.UserInfos;

public class Listing_Offers extends Activity  {
    private PullToRefreshRecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter_new_card;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "offers_listing";
    private ProgressView progressview;
    private HashMap<Integer,String> mapgoods= new HashMap<>();
    private ArrayList<Offer> listoffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_activity);

        progressview = (ProgressView) findViewById(R.id.progress_view);
        mRecyclerView = (PullToRefreshRecyclerView ) findViewById(R.id.my_recycler_view);
        if (Connectivity.Checkinternet(this)) {
            ConfigureRecycleView();
            getData(Listing_Offers.this);
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
//        mRecyclerView.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
//            @Override
//            public void onLoadMoreItems() {
//                getData(Listing_Offers.this);
//            }
//        });
        // set OnRefreshListener
        mRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getData(Listing_Offers.this);
            }
        });

        // set loadmore String
                mRecyclerView.setLoadmoreString("loading");

        // set loadmore enable, onFinishLoading(can load more? , select before item)
                mRecyclerView.onFinishLoading(true, false);

        mRecyclerView.setEmptyView(View.inflate(this,R.layout.empty_view_recyclerview,null));


    }

    private void getData(Activity activity){
        class GetData extends AsyncTask<String,String,String> {
            Activity activity;
            private String responseBody;
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
            protected String doInBackground(String... params) {

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
                try {
                        // set up post data
                        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                        nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
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

        GetData gd = new GetData(activity);
        gd.execute(Links.getRootFolder()+"getofferstoid.php", String.valueOf(UserInfos.getConnecteduser().getId()));
        //String.valueOf(UserInfos.getConnecteduser().getId())
    }

    public void showData(){
            adapter_new_card = new OffersAdapter(listoffers,Listing_Offers.this);
            mRecyclerView.setAdapter(adapter_new_card);
            mRecyclerView.setOnRefreshComplete();
    }


    private void parseJSON(String json){
        ArrayList<Offer> resultlist;
        if (!json.equals(""))
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("offer");

            resultlist = new ArrayList<>();
            Offer offer;
            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                offer=new Offer(j);
                resultlist.add(offer);
            }
            listoffers =resultlist;
            showData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent(getApplication(),Home_activity.class);
        startActivity(intent);
    }

    public void showuser(Bundle bundle)
    {
        SampleDialogFragment fragment
            = SampleDialogFragment.newInstance(
            4,
            4.0f,
            true,
            false
        );
        fragment.setbundle(bundle);
        fragment.show(getFragmentManager(), "blur_sample");
    }

}
