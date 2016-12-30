package org.artek.app.game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.R;

public class ScannerQRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    String scanningURL;
    private QRCodeReaderView mydecoderview;
    public String name = "ScannerQR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        } else {
            /*
            mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
            mydecoderview.setOnQRCodeReadListener(this);
            mydecoderview.getCameraManager().startPreview();
            */
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
                    mydecoderview.setOnQRCodeReadListener(this);
                    mydecoderview.getCameraManager().startPreview();


                } else {

                }
                return;
            }

        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        scanningURL=text;

        Intent intent = new Intent();
        intent.putExtra("SCAN_RESULT", scanningURL);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mydecoderview.getCameraManager().stopPreview();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}