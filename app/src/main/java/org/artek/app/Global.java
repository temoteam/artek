package org.artek.app;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.artek.app.account.AccountManager;

public class Global {

    public static AccountManager accountManager;
    public static User user;
    public static SharedPreferences sharedPreferences;
    public static Activity activity;



    public static void initilizate(Activity activity) {
        accountManager = new AccountManager(activity);
        sharedPreferences = activity.getSharedPreferences("artek_pref", Context.MODE_PRIVATE);
    }

    private class User {
        String id;
        String token;
        String firstname;
        String lastname;
    }

    public class SharedPreferencesTags{
        public final static String CAMP = "camp";
        public final static String LAST_TOKEN = "last_token";
        public final static String LAST_ID = "last_id";
        public final static String THEME_ID = "theme_id";
    }

    public interface appInterface{
        void returner();
    }

}
