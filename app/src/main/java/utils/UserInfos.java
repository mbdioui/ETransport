package utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.rey.material.app.ThemeManager;

import Beans.User;


/**
 * Created by mohamed salah on 08/10/2016.
 */

 public class UserInfos extends Application{
    private static User  connecteduser;
    public static Boolean IsConnected = false;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //Bootstrap library
    @Override public void onCreate() {
        super.onCreate();
        ThemeManager.init(this, 1, 0, null);
        TypefaceProvider.registerDefaultIconSets();
    }

    public final static User getConnecteduser() {
        return connecteduser;
    }

    public final static void setConnecteduser(User connecteduser) {
        UserInfos.connecteduser = connecteduser;
    }

}
