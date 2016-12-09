package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.rey.material.widget.Spinner;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Form_Infos_Client extends android.support.v4.app.Fragment {
    private Spinner spinnercategories;
    private List<String> l1;
    private HashMap<String,String> map1;
    private HashMap<String,String> map2;
    private List<String> l2;
    private List<String> l3;
    private Spinner spinnermeans;
    private Spinner spinnergoods;
    private boolean spinneractive =false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_form_info_client, container, false);
        GetTransportTypesTask task= new GetTransportTypesTask(getActivity());
                task.execute(utils.Links.getRootFolder()+"getcategories.php","");
        return v;
    }

    private void settingup(View v) {
//        String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};


//        Set setOfKeys = map1.keySet();
//        Iterator iterator = setOfKeys.iterator();
//        while (iterator.hasNext()) {
//            String key = (String) iterator.next();
//            l1.add(key);
//        }
        spinnergoods = (Spinner) v.findViewById(R.id.spinner_goods);
//        spinnercategories = (Spinner) v.findViewById(R.id.spinner_type_means);
        spinnermeans = (Spinner) v.findViewById(R.id.spinner_means);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, l1);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnercategories.setAdapter(adapter);

        Map.Entry<String,String> entry=map1.entrySet().iterator().next();
        String key= entry.getKey();
        String value=entry.getValue();
        l2 = new ArrayList<String>();
        l2.add("---");
        Iterator entries2 = map2.entrySet().iterator();
        while (entries2.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries2.next();
            l2.add(thisEntry.getKey().toString());

        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, l2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnermeans.setAdapter(adapter2);

//        spinnercategories.post(new Runnable() {
//            @Override public void run() {
//                spinnercategories.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(Spinner parent, View view, int position, long id) {
//                        l2.clear();
//                        String text = spinnercategories.getSelectedItem().toString();
//                        String ID = "";
//                        Iterator entries = map1.entrySet().iterator();
//                        while (entries.hasNext()) {
//                            Map.Entry thisEntry = (Map.Entry) entries.next();
//                            if (thisEntry.getKey().equals(text))
//                                ID = thisEntry.getValue().toString();
//                        }
//                        Iterator entries2 = map2.entrySet().iterator();
//                        while (entries2.hasNext()) {
//                            Map.Entry thisEntry = (Map.Entry) entries2.next();
//                            if (thisEntry.getValue().equals(ID)) {
//                                l2.add(thisEntry.getKey().toString());
//                            }
//                        }
//
//                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, l2);
//                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnermeans.setAdapter(adapter2);
//                    }
//
//
//                });
//            }});

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, l3);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnergoods.setAdapter(adapter3);

//        spinnercategories.post(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<3;i++)
//                {  if(l1.get(i).equals("terreste"))
//                    spinnercategories.setSelection(i);
//                }
//            }
//        });
//        spinnercategories.setEnabled(false);
    }

    public class GetTransportTypesTask extends AsyncTask<String, String, String> {

        private final Activity activity;
        private String responseBody;
        private AlertDialog progdialog;
        private JSONArray CategoriesJsonArray;
        private JSONObject CategoriesJsonObject;
        private JSONArray meansJsonArray;
        private JSONObject meansJsonObject;
        private JSONArray goodsJsonArray;
        private JSONObject goodsJsonObject;


        public GetTransportTypesTask(Activity act) {
            progdialog = new SpotsDialog(act,R.style.CustomSpotDialog);
            activity=act;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);
            try {
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("email", params[1]));
                post.setEntity(new UrlEncodedFormEntity(nameValuePair, org.apache.http.protocol.HTTP.UTF_8));
                HttpResponse response = client.execute(post);
                responseBody = EntityUtils.toString(response.getEntity());

            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
            e.printStackTrace();
            }


            return responseBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progdialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progdialog.dismiss();
            JSONObject json = null;
            l1= new ArrayList<>();
            l3 = new ArrayList<>();
            l3.add("---");
            map1 =  new HashMap<String,String>();
            map2 =  new HashMap<String,String>();
            if(result!=null) {
                try {
                    json = new JSONObject(result);
                    if (json.getString("error").equals("0")) {
                        CategoriesJsonArray = json.getJSONArray("categories");
                        meansJsonArray=json.getJSONArray("means");
                        goodsJsonArray = json.getJSONArray("goods");
                        for(int j=0;j<meansJsonArray.length();j++)
                        {
                            meansJsonObject = meansJsonArray.getJSONObject(j);
                            map2.put(String.valueOf(meansJsonObject.getString("means_title"))
                                    ,String.valueOf(meansJsonObject.getString("idt_transport")));
                        }
                        for(int i=0;i<CategoriesJsonArray.length();i++)
                        {
                            CategoriesJsonObject = CategoriesJsonArray.getJSONObject(i);
                            map1.put(String.valueOf(CategoriesJsonObject.getString("transport_type"))
                                   ,String.valueOf(CategoriesJsonObject.getString("idt__type")));
                        }
                        for(int k=0;k<goodsJsonArray.length();k++)
                        {
                            goodsJsonObject = goodsJsonArray.getJSONObject(k);
                            l3.add(String.valueOf(goodsJsonObject.getString("goods_desc")));
                        }
                        settingup(getView());
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
            else
            {
                Toast.makeText(activity.getBaseContext(), "echec de connexion,veuillez ressayer de relancer l'activité", Toast.LENGTH_LONG).show();
            }
        }

    }

}
