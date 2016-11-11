package org.artek.app;


import android.app.Activity;

import org.artek.app.account.AccountManager;
import org.artek.app.main.LogReader;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class Global {
    public static AccountManager accountManager;
    public static HashMap<String,String> userInfo;
    public static LogReader logReader;

    public static void initilizate(Activity activity) throws FileNotFoundException {
        accountManager = new AccountManager(activity);
       logReader = new LogReader(activity);
    }
}
