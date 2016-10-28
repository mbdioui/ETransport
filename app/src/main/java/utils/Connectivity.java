package utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mohamed salah on 24/09/2016.
 */

public class Connectivity {
    public static boolean Checkinternet(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());
    }
    public static boolean checkGPS (Context context)
    {
        LocationManager lm =
                (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        return  lm.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }


}
