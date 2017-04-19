package ru.temoteam.artek.app.game;

import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ru.temoteam.artek.app.AnalyticsApplication;

public class ScannerQRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    String scanningURL;
    String name = "QR";
    boolean hasCamera = false;
    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ru.temoteam.artek.app.R.layout.activity_scanner_qr);


        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras > 0) {
            hasCamera = true;
        }
        if (hasCamera) {
            mydecoderview = (QRCodeReaderView) findViewById(ru.temoteam.artek.app.R.id.qrdecoderview);
            mydecoderview.setOnQRCodeReadListener(this);
        } else {
            Toast.makeText(getApplicationContext(), getString(ru.temoteam.artek.app.R.string.no_camera), Toast.LENGTH_LONG).show();
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
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
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