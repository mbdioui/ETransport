package Beans;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohamed salah on 12/12/2016.
 */

public class Transport {
    private int user_id;
    private int transport_id;
    private Date transport_date_go;
    private Date transport_date_arrival;
    private Date transport_date_go_min;
    private Date transport_date_go_max;
    private Date transport_date_arrival_min;
    private Date transport_date_arrival_max;
    private String transport_text;
    private Date transport_date_add;
    private int transport_status;
    private int id_type_goods;
    private String address_from;
    private String address_to;
    private float start_pos_lgt;
    private float start_pos_lat;
    private float end_pos_lgt;
    private float end_pos_lat;
    private int type_pub;
    private int means_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(int transport_id) {
        this.transport_id = transport_id;
    }

    public Date getTransport_date_go() {
        return transport_date_go;
    }

    public void setTransport_date_go(Date transport_date_go) {
        this.transport_date_go = transport_date_go;
    }

    public Date getTransport_date_arrival() {
        return transport_date_arrival;
    }

    public void setTransport_date_arrival(Date transport_date_arrival) {
        this.transport_date_arrival = transport_date_arrival;
    }

    public Date getTransport_date_go_min() {
        return transport_date_go_min;
    }

    public void setTransport_date_go_min(Date transport_date_go_min) {
        this.transport_date_go_min = transport_date_go_min;
    }

    public Date getTransport_date_go_max() {
        return transport_date_go_max;
    }

    public void setTransport_date_go_max(Date transport_date_go_max) {
        this.transport_date_go_max = transport_date_go_max;
    }

    public Date getTransport_date_arrival_min() {
        return transport_date_arrival_min;
    }

    public void setTransport_date_arrival_min(Date transport_date_arrival_min) {
        this.transport_date_arrival_min = transport_date_arrival_min;
    }

    public Date getTransport_date_arrival_max() {
        return transport_date_arrival_max;
    }

    public void setTransport_date_arrival_max(Date transport_date_arrival_max) {
        this.transport_date_arrival_max = transport_date_arrival_max;
    }

    public String getTransport_text() {
        return transport_text;
    }

    public void setTransport_text(String transport_text) {
        this.transport_text = transport_text;
    }

    public Date getTransport_date_add() {
        return transport_date_add;
    }

    public void setTransport_date_add(Date transport_date_add) {
        this.transport_date_add = transport_date_add;
    }

    public int getTransport_status() {
        return transport_status;
    }

    public void setTransport_status(int transport_status) {
        this.transport_status = transport_status;
    }

    public int getId_type_goods() {
        return id_type_goods;
    }

    public void setId_type_goods(int id_type_goods) {
        this.id_type_goods = id_type_goods;
    }

    public String getAddress_from() {
        return address_from;
    }

    public void setAddress_from(String address_from) {
        this.address_from = address_from;
    }

    public String getAddress_to() {
        return address_to;
    }

    public void setAddress_to(String address_to) {
        this.address_to = address_to;
    }

    public float getStart_pos_lgt() {
        return start_pos_lgt;
    }

    public void setStart_pos_lgt(float start_pos_lgt) {
        this.start_pos_lgt = start_pos_lgt;
    }

    public float getStart_pos_lat() {
        return start_pos_lat;
    }

    public void setStart_pos_lat(float start_pos_lat) {
        this.start_pos_lat = start_pos_lat;
    }

    public float getEnd_pos_lgt() {
        return end_pos_lgt;
    }

    public void setEnd_pos_lgt(float end_pos_lgt) {
        this.end_pos_lgt = end_pos_lgt;
    }

    public float getEnd_pos_lat() {
        return end_pos_lat;
    }

    public void setEnd_pos_lat(float end_pos_lat) {
        this.end_pos_lat = end_pos_lat;
    }

    public int getType_pub() {
        return type_pub;
    }

    public void setType_pub(int type_pub) {
        this.type_pub = type_pub;
    }

    public int getMeans_id() {
        return means_id;
    }

    public void setMeans_id(int means_id) {
        this.means_id = means_id;
    }

    public Transport(){};

    public Transport(int user_id, int transport_id, Date date_go, Date transport_date_arrival, Date transport_date_go_min, Date transport_date_go_max, Date transport_date_arrival_min, Date transport_date_arrival_max, String transport_text, Date transport_date_add, int transport_status, int id_type_goods, String address_from, String address_to, float start_pos_lgt, float start_pos_lat, float end_pos_lgt, float end_pos_lat, int type_pub, int means_id) {
        this.user_id = user_id;
        this.transport_id = transport_id;
        this.transport_date_go = date_go;
        this.transport_date_arrival = transport_date_arrival;
        this.transport_date_go_min = transport_date_go_min;
        this.transport_date_go_max = transport_date_go_max;
        this.transport_date_arrival_min = transport_date_arrival_min;
        this.transport_date_arrival_max = transport_date_arrival_max;
        this.transport_text = transport_text;
        this.transport_date_add = transport_date_add;
        this.transport_status = transport_status;
        this.id_type_goods = id_type_goods;
        this.address_from = address_from;
        this.address_to = address_to;
        this.start_pos_lgt = start_pos_lgt;
        this.start_pos_lat = start_pos_lat;
        this.end_pos_lgt = end_pos_lgt;
        this.end_pos_lat = end_pos_lat;
        this.type_pub = type_pub;
        this.means_id = means_id;
    }

    public Transport (JSONObject Json,int intformat)
    {
        try {
            this.user_id = (Json.getInt("user_id"));
            this.transport_id = (Json.getInt("transport_id"));
            DateFormat format;
            if(intformat==1)
                format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            else
                format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss",Locale.ENGLISH);
//            "transport_date_arrival" -> "Oct 27, 2016 12:00:00 AM"
            try {
                if(Json.has("transport_date_go"))
                {
                    String date_go = (Json.getString("transport_date_go"));
                    if (!date_go.equals("0000-00-00") && !date_go.equals("null"))
                        this.transport_date_go = format.parse(date_go);
                }
                if(Json.has("transport_date_arrival")) {
                    String date_arrival = (Json.getString("transport_date_arrival"));
                    if (!date_arrival.equals("0000-00-00") && !date_arrival.equals("null"))
                        this.transport_date_arrival = format.parse(date_arrival);
                }
                if(Json.has("transport_date_go_min")) {
                    String date_go_min = (Json.getString("transport_date_go_min"));
                    if (!date_go_min.equals("0000-00-00") && !date_go_min.equals("null"))
                        this.transport_date_go_min = format.parse(date_go_min);
                }
                if(Json.has("transport_date_go_max")) {
                    String date_go_max = (Json.getString("transport_date_go_max"));
                    if (!date_go_max.equals("0000-00-00") && !date_go_max.equals("null"))
                        this.transport_date_go_max = format.parse(date_go_max);
                }
                if(Json.has("transport_date_arrival_min")) {
                    String date_arrival_min = (Json.getString("transport_date_arrival_min"));
                    if (!date_arrival_min.equals("0000-00-00") && !date_arrival_min.equals("null"))
                        this.transport_date_arrival_min = format.parse(date_arrival_min);
                }
                if(Json.has("transport_date_arrival_max")) {
                    String date_arrival_max = (Json.getString("transport_date_arrival_max"));
                    if (!date_arrival_max.equals("0000-00-00") && !date_arrival_max.equals("null"))
                        this.transport_date_arrival_max = format.parse(date_arrival_max);
                }
                if(Json.has("transport_date_add")) {
                    String date_add = (Json.getString("transport_date_add"));
                    if (!date_add.equals("0000-00-00") && !date_add.equals("null"))
                        this.transport_date_add = format.parse(date_add);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.transport_text = (Json.getString("transport_text"));
            if (!Json.getString("transport_status").equals(""))
                this.transport_status = (Json.getInt("transport_status"));
            if (!Json.getString("id_type_goods").equals("null"))
                this.id_type_goods = (Json.getInt("id_type_goods"));
            this.address_from =(Json.getString("address_from"));
            this.address_to = (Json.getString("address_to"));

            DecimalFormat form = new DecimalFormat("00.0000");
            this.start_pos_lgt =Float.parseFloat(Json.getString("start_pos_lgt"));
            this.start_pos_lat = Float.parseFloat(Json.getString("start_pos_lat"));
            this.end_pos_lgt = Float.parseFloat(Json.getString("end_pos_lgt"));
            this.end_pos_lat = Float.parseFloat(Json.getString("end_pos_lat"));
            this.type_pub = (Json.getInt("type_pub"));
            if (!Json.getString("means_id").equals("null"))
                this.means_id = (Json.getInt("means_id"));

    }
    catch (JSONException e) {
        e.printStackTrace();
    }

    }
    public String getTransport()
    {
        Gson gson= new Gson();
        return gson.toJson(this);
    }
}
