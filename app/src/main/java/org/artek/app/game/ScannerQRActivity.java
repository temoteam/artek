package org.artek.app.game;

        import android.Manifest;
        import android.content.Intent;
        import android.graphics.PointF;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.Toast;

        import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

        import org.artek.app.R;

public class ScannerQRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    String scanningURL;
    private QRCodeReaderView mydecoderview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
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