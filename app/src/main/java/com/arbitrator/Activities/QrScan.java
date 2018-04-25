package com.arbitrator.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.arbitrator.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrScan extends AppCompatActivity {

    public Button asd;
    public Barcode bar;

    SurfaceView camP;
    public int f = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

//        asd = findViewById(R.id.asd);
        camP = findViewById(R.id.camera_preview);

//        asd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("sfd", bar.displayValue);
//                Toast.makeText(getApplicationContext(), bar.displayValue, Toast.LENGTH_LONG).show();
//            }
//        });

        createcams();

    }

    //CREATING CAMERA LENS
    private void createcams() {
        BarcodeDetector bD = new BarcodeDetector.Builder(this).build();
        final CameraSource cS = new CameraSource.Builder(this, bD)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        camP.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(QrScan.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cS.start(camP.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cS.stop();
            }
        });


        bD.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> br = detections.getDetectedItems();
                if (br.size() > 0 && f < 1) {
                    bar = br.valueAt(0);
                    Login.barcode = bar.displayValue;
                    f = 1;
                    Log.e("sfd", bar.displayValue);
                    finish();
//                    Toast.makeText(getApplicationContext(), bar.displayValue, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}


