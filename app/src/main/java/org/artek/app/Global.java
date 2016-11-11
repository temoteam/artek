package org.artek.app;


import android.app.Activity;

import org.artek.app.account.AccountManager;

import java.util.HashMap;

public class Global {
    public static AccountManager accountManager;
    public static HashMap<String, String> userInfo;

    public static void initilizate(Activity activity) {
        accountManager = new AccountManager(activity);
    }
}
