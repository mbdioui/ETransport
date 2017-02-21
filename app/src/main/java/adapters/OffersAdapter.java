package adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.squareup.picasso.Picasso;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import Beans.Offer;
import Beans.Transport;
import Beans.User;
import tasks.SendNotificationFromTransporter;
import tn.android.etransport.etransport.Listing_Offers;
import tn.android.etransport.etransport.R;
import utils.Links;

/**
 * Created by mohamed salah on 14/12/2016.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private ArrayList<Offer> items;
    private Context context;

    public OffersAdapter(ArrayList<Offer> list
            , Context context){
        super();
        this.context=context;
        items = new ArrayList<>();
        if (list!=null)
            for(int i =0; i<list.size(); i++){
                    items.add(list.get(i));
            }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Offer item =  items.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.FRANCE);
        if (item.getDate_offer()!=null)
            holder.dateTextView.setText(String.valueOf(dateFormatter.format(item.getDate_offer())));
        holder.Price_offer.setText(holder.Price_offer.getText()+String.valueOf(item.getPrice()));

        holder.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Offer item=items.get(position);
//                Toast.makeText(context,String.valueOf(item.getId_transport()),Toast.LENGTH_LONG).show();
                ConfirmOffertask task= new ConfirmOffertask();
                task.execute(Links.getRootFolder()+"confirm_transport.php",String.valueOf(item.getId_transporter()),String.valueOf(item.getId_transport()));
            }
        });

        getusertask task= new getusertask(holder);
        task.execute(Links.getRootFolder()+"getuser.php",String.valueOf(item.getId_transporter()));
        gettransporttask tasktransport = new gettransporttask(holder);
        tasktransport.execute(Links.getRootFolder()+"gettransportbyid.php",String.valueOf(item.getId_transport()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTextView;
        public BootstrapCircleThumbnail thumbnail;
        public TextView NameTransporter;
        public TextView AddressFrom;
        public TextView AddressTo;
        public TextView datefrom;
        public TextView dateto;
        public TextView Price_offer;
        public BootstrapButton confirm_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_offer);
            thumbnail = (BootstrapCircleThumbnail) itemView.findViewById(R.id.thumbnail_offer_card);
            NameTransporter =(TextView) itemView.findViewById(R.id.name_transporter_offer);
            AddressFrom =(TextView) itemView.findViewById(R.id.address_from_offer);
            AddressTo =(TextView) itemView.findViewById(R.id.address_to_offer);
            datefrom =(TextView) itemView.findViewById(R.id.date_depart_offer);
            dateto =(TextView) itemView.findViewById(R.id.date_arrive_offer);
            Price_offer = (TextView) itemView.findViewById(R.id.price_offer);
            confirm_btn = (BootstrapButton) itemView.findViewById(R.id.confirm_offer_btn);
        }
    }
    public class getusertask extends AsyncTask<String, String, String> {

        private ViewHolder holder;
        private String responseBody;


        public getusertask(ViewHolder holder) {
            super();
            this.holder=holder;
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject j = jsonObject.getJSONObject("user");
                User user=new  User(j);
                putUserImage(user,holder);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public class gettransporttask extends AsyncTask<String, String, String> {

        private ViewHolder holder;
        private String responseBody;


        public gettransporttask(ViewHolder holder) {
            super();
            this.holder=holder;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

            try {
                // set up post data
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("transport_id", params[1]));

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

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jarray = jsonObject.getJSONArray("transport");
                JSONObject j = jarray.getJSONObject(0);
                Transport transport=new  Transport(j,1);
                puttransportfields(transport,holder);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public class ConfirmOffertask extends AsyncTask<String, String, String> {

        private String responseBody;

        public ConfirmOffertask() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

            try {
                // set up post data
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("transporter_id", params[1]));
                nameValuePair.add(new BasicNameValuePair("transport_id",params[2]));

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

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jarray = jsonObject.getJSONArray("user");
                JSONObject j = jarray.getJSONObject(0);
                User user=new  User(j);
                Bundle b = new Bundle();
                b.putString("fname",user.getF_name());
                b.putString("lname",user.getL_name());
                b.putString("mail",user.getMail());
                b.putString("phone",user.getPhone());
                b.putString("picture",user.getUser_picture());
                if(context instanceof Listing_Offers)
                {
                    Listing_Offers activity=((Listing_Offers)context);
                    activity.showuser(b);

                    SendNotificationFromTransporter notification= new SendNotificationFromTransporter(context);
                    notification.execute(Links.getRootFolder()+"sendPushNotification.php","offre acceptée"
                            ,"l'affréteur a acceptée votre offre" ,String.valueOf(user.getId()));



                }
                }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void puttransportfields(Transport transport,ViewHolder holder)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy ", Locale.FRANCE);

        holder.AddressFrom.setText(holder.AddressFrom.getText()+transport.getAddress_from());
        holder.AddressTo.setText(holder.AddressTo.getText()+transport.getAddress_to());
        if (transport.getTransport_date_go()==null)
            holder.datefrom.setText(holder.datefrom.getText()+dateFormatter.format(transport.getTransport_date_go_max()));
        else
            holder.datefrom.setText(holder.datefrom.getText()+dateFormatter.format(transport.getTransport_date_go()));
        if(transport.getTransport_date_arrival()==null)
            holder.dateto.setText(holder.dateto.getText()+dateFormatter.format(transport.getTransport_date_arrival_max()));
        else
            holder.dateto.setText(holder.dateto.getText()+dateFormatter.format(transport.getTransport_date_arrival()));

    }




    private void putUserImage(User user,ViewHolder holder) {
        if (!user.getUser_picture().equals("null"))
        {
            Picasso.with(context).load(Links.getProfilePictures()+ user.getUser_picture())
                    .into(holder.thumbnail);
        }
        putUsername(user,holder);

    }
    private void putUsername(User user,ViewHolder holder) {
        holder.NameTransporter.setText(String.valueOf(user.getF_name()));
    }
}