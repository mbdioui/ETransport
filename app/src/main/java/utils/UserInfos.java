package utils;

import android.app.Application;

import Beans.User;


/**
 * Created by mohamed salah on 08/10/2016.
 */

 public class UserInfos extends Application{
    public final static User getConnecteduser() {
        return connecteduser;
    }

    public final static void setConnecteduser(User connecteduser) {
        UserInfos.connecteduser = connecteduser;
    }

    private static User  connecteduser;


}
