package Beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohamed salah on 19/01/2017.
 */

public class Offer {
    private int id_offer;
    private float price;
    private int id_transport;
    private int id_transporter;
    private Date date_offer;

    public Offer(){}
    public Offer(int id_offer, float price, int id_transport, int id_transporter, Date date_offer) {
        this.id_offer = id_offer;
        this.price = price;
        this.id_transport = id_transport;
        this.id_transporter = id_transporter;
        this.date_offer = date_offer;
    }

    public Offer(float price, int id_transport, int id_transporter) {
        this.price = price;
        this.id_transport = id_transport;
        this.id_transporter = id_transporter;
    }

    public int getId_offer() {
        return id_offer;
    }

    public void setId_offer(int id_offer) {
        this.id_offer = id_offer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId_transport() {
        return id_transport;
    }

    public void setId_transport(int id_transport) {
        this.id_transport = id_transport;
    }

    public int getId_transporter() {
        return id_transporter;
    }

    public void setId_transporter(int id_transporter) {
        this.id_transporter = id_transporter;
    }

    public Date getDate_offer() {
        return date_offer;
    }

    public void setDate_offer(Date date_offer) {
        this.date_offer = date_offer;
    }
    public Offer (JSONObject Json)
    {
        try {
            this.id_offer = (Json.getInt("id_offer"));
            this.price = (Json.getLong("price"));
            this.id_transport = (Json.getInt("id_transport"));
            this.id_transporter = (Json.getInt("id_transporter"));
            DateFormat format;
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.FRANCE);
            try {
                if (Json.has("date_offer")) {
                    String date_offer = (Json.getString("date_offer"));
                    if (!date_offer.equals("0000-00-00") && !date_offer.equals("null"))
                        this.date_offer = format.parse(date_offer);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
