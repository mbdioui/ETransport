package tn.android.etransport.etransport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohamed salah on 13/10/2016.
 */


public class AddCityTask extends AsyncTask<String, String, String> {

    private Context context;
    private String  CityName;
    private  ProgressDialog progdialog;
    public Context getCntx() {
        return context;
    }

    public void setCntx(Context context) {
        this.context = context;
    }

    public AddCityTask(Activity activity)
    {
        progdialog = new ProgressDialog(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progdialog.setMessage("changement en cours");
        progdialog.show();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progdialog.dismiss();
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            if(json.getString("error").equals("0"))
                Toast.makeText(context,"Insertion Done",Toast.LENGTH_LONG).show();
            else
            if(json.getString("error").equals("2"))
                Toast.makeText(context,"Mail already exist",Toast.LENGTH_LONG).show();
                //  String id=json.getString("client_id").toString();
            else
            if(json.getString("error").equals("-1"))
                Toast.makeText(context,"Connexion problem",Toast.LENGTH_LONG).show();
        }
        catch (JSONException e)        {}

    }


}

