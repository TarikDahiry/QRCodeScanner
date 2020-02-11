package com.topqal.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	SurfaceView srfVw;
	CameraSource cmrSrc;
	TextView txtVw;
	BarcodeDetector brCdD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		srfVw = findViewById(R.id.srfVw);
		txtVw = findViewById(R.id.txtVw);

		brCdD = new BarcodeDetector.Builder(this)
				.setBarcodeFormats(Barcode.QR_CODE).build();

		cmrSrc = new CameraSource.Builder(this, brCdD)
				.setRequestedPreviewSize(640,480).build();

		srfVw.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
			//	if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA != PackageManager.PERMISSION_GRANTED)) return;
				try {
					cmrSrc.start(holder);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
              cmrSrc.stop();
			}
		});


		brCdD.setProcessor(new Detector.Processor<Barcode>() {
			@Override
			public void release() {

			}

			@Override
			public void receiveDetections(Detector.Detections<Barcode> detections) {
				final SparseArray<Barcode> arry = detections.getDetectedItems();
				if (arry.size() != 0) {
					txtVw.post(new Runnable() {
						@Override
						public void run() {
							Vibrator vbr = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
							assert vbr != null;
							vbr.vibrate(1000);
							txtVw.setText(arry.valueAt(0).displayValue);
						}
					});
				}
			}
		});
	}
}
