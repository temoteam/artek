package ru.temoteam.artek.app;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import ru.temoteam.artek.app.account.AccountManager;

public class Global {

    public static AccountManager accountManager;
    public static SharedPreferences sharedPreferences;
    public static Activity activity;
    public static String nowPlaying;

    public static String server = "https://debug.azurecom.ru/";

    public static String SAVED = "qr_saves";

    public static void initilizate(Activity activity) {
        accountManager = new AccountManager(activity);
        sharedPreferences = activity.getSharedPreferences("artek_pref", Context.MODE_PRIVATE);
    }

    public interface appInterface {
        void returner();
    }

    public class SharedPreferencesTags{
        public final static String CAMP = "camp";
        public final static String LAST_TOKEN = "last_token";
        public final static String LAST_ID = "last_id";
        public final static String FIRST_NAME = "first_name";
        public final static String LAST_NAME = "last_name";
        public final static String AVA_URL = "ava_img_url";
        public final static String STATUS = "user_status";
        public final static String THEME_ID = "theme_id";
        public final static String ALERT_ID = "alert_id";
        public final static String GOOGLE_TOKEN = "google_token";
        public final static String IS_CALLBACK_SAVED = "save_callback";
    }

}
