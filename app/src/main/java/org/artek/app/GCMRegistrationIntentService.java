package org.artek.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class GCMRegistrationIntentService extends IntentService {
    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    String token = null;

    //Class constructor
    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {
        //Registration complete intent initially null
        Intent registrationComplete;

        //Register token is also null
        //we will get the token on successfull registration

        try {
            //Creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server

            new Login().execute();

            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            //If any error occurred

            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }



    class Login extends AsyncTask<Void, Void, Byte> {

        @Override
        protected Byte doInBackground(Void... params) {

            try {
                rawQuery(new URL(getString(R.string.main_domain) + "/artek/notify/addClient.php?token=" + token));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private String rawQuery(URL url) throws IOException {
            InputStream input = url.openStream();
            Scanner in = new Scanner(input);
            return in.nextLine();
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
        }
    }
}