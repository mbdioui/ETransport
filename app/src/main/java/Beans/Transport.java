package Beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
    private Date date_go;
    private Date date_arrival;
    private Date date_go_min;
    private Date date_go_max;
    private Date date_arrival_min;
    private Date date_arrival_max;
    private String text;
    private Date date_add;
    private int status;
    private int type_goods;
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

    public Date getDate_go() {
        return date_go;
    }

    public void setDate_go(Date date_go) {
        this.date_go = date_go;
    }

    public Date getDate_arrival() {
        return date_arrival;
    }

    public void setDate_arrival(Date date_arrival) {
        this.date_arrival = date_arrival;
    }

    public Date getDate_go_min() {
        return date_go_min;
    }

    public void setDate_go_min(Date date_go_min) {
        this.date_go_min = date_go_min;
    }

    public Date getDate_go_max() {
        return date_go_max;
    }

    public void setDate_go_max(Date date_go_max) {
        this.date_go_max = date_go_max;
    }

    public Date getDate_arrival_min() {
        return date_arrival_min;
    }

    public void setDate_arrival_min(Date date_arrival_min) {
        this.date_arrival_min = date_arrival_min;
    }

    public Date getDate_arrival_max() {
        return date_arrival_max;
    }

    public void setDate_arrival_max(Date date_arrival_max) {
        this.date_arrival_max = date_arrival_max;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate_add() {
        return date_add;
    }

    public void setDate_add(Date date_add) {
        this.date_add = date_add;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType_goods() {
        return type_goods;
    }

    public void setType_goods(int type_goods) {
        this.type_goods = type_goods;
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

    public Transport(int user_id, int transport_id, Date date_go, Date date_arrival, Date date_go_min, Date date_go_max, Date date_arrival_min, Date date_arrival_max, String text, Date date_add, int status, int type_goods, String address_from, String address_to, float start_pos_lgt, float start_pos_lat, float end_pos_lgt, float end_pos_lat, int type_pub, int means_id) {
        this.user_id = user_id;
        this.transport_id = transport_id;
        this.date_go = date_go;
        this.date_arrival = date_arrival;
        this.date_go_min = date_go_min;
        this.date_go_max = date_go_max;
        this.date_arrival_min = date_arrival_min;
        this.date_arrival_max = date_arrival_max;
        this.text = text;
        this.date_add = date_add;
        this.status = status;
        this.type_goods = type_goods;
        this.address_from = address_from;
        this.address_to = address_to;
        this.start_pos_lgt = start_pos_lgt;
        this.start_pos_lat = start_pos_lat;
        this.end_pos_lgt = end_pos_lgt;
        this.end_pos_lat = end_pos_lat;
        this.type_pub = type_pub;
        this.means_id = means_id;
    }

    public Transport (JSONObject Json)
    {
        try {
            this.user_id = (Json.getInt("user_id"));
            this.transport_id = (Json.getInt("transport_id"));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            try {
                String date_go = (Json.getString("transport_date_go"));
                if(!date_go.equals("0000-00-00")&& !date_go.equals("null"))
                    this.date_go = format.parse(date_go);
                String date_arrival = (Json.getString("transport_date_arrival"));
                if(!date_arrival.equals("0000-00-00")&& !date_arrival.equals("null"))
                    this.date_arrival = format.parse(date_arrival);
                String date_go_min = (Json.getString("transport_date_go_min"));
                if(!date_go_min.equals("0000-00-00")&& !date_go_min.equals("null"))
                    this.date_go_min = format.parse(date_go_min);
                String date_go_max = (Json.getString("transport_date_go_max"));
                if(!date_go_max.equals("0000-00-00")&& !date_go_max.equals("null"))
                    this.date_go_max = format.parse(date_go_max);
                String date_arrival_min = (Json.getString("transport_date_arrival_min"));
                if(!date_arrival_min.equals("0000-00-00")&& !date_arrival_min.equals("null"))
                    this.date_arrival_min = format.parse(date_arrival_min);
                String date_arrival_max = (Json.getString("transport_date_arrival_max"));
                if(!date_arrival_max.equals("0000-00-00")&& !date_arrival_max.equals("null"))
                    this.date_arrival_max = format.parse(date_arrival_max);
                String date_add = (Json.getString("transport_date_add"));
                if(!date_add.equals("0000-00-00")&& !date_add.equals("null"))
                    this.date_add = format.parse(date_add);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.text = (Json.getString("transport_text"));
            if (!Json.getString("transport_status").equals(""))
                this.status = (Json.getInt("transport_status"));
            if (!Json.getString("id_type_goods").equals("null"))
                this.type_goods = (Json.getInt("id_type_goods"));
            this.address_from =(Json.getString("address_from"));
            this.address_to = (Json.getString("address_to"));
            this.start_pos_lgt = (Json.getLong("start_pos_lgt"));;
            this.start_pos_lat = (Json.getLong("start_pos_lat"));;
            this.end_pos_lgt = (Json.getLong("end_pos_lgt"));;
            this.end_pos_lat = (Json.getLong("end_pos_lat"));;
            this.type_pub = (Json.getInt("type_pub"));
            if (!Json.getString("means_id").equals("null"))
                this.means_id = (Json.getInt("means_id"));

    }
    catch (JSONException e) {
        e.printStackTrace();
    }

    }
}
