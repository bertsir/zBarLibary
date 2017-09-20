package com.arseeds.ar;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.arseeds.ar.Qr.Image;
import com.arseeds.ar.view.ScanView;

public class QRActivity extends Activity {

    private CameraPreview cp;
    private ImageView iv_scan_line;
    private SoundPool soundPool;
    private ScanView sv;
    private ImageView mo_scanner_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_qr);
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cp != null) {
            cp.setScanCallback(resultCallback);
            cp.start();
        }
        sv.onResume();
    }

    private void initView() {
        cp = (CameraPreview) findViewById(R.id.cp);
        iv_scan_line = (ImageView) findViewById(R.id.iv_scan_line);

        //bi~
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.qrcode, 1);

        sv = (ScanView) findViewById(R.id.sv);
        sv.startScan();

        mo_scanner_back = (ImageView) findViewById(R.id.mo_scanner_back);

    }

    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(String result) {
            soundPool.play(1, 1, 1, 0, 0, 1);
            Intent videoPath = new Intent().putExtra("QRcontent", result);
            setResult(RESULT_OK, videoPath);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cp.stop();
        soundPool.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cp != null) {
            cp.stop();
        }

        sv.onPause();
    }

    public void scanImage(String url){
        Image image = new Image();
        Image convert = image.convert(url);

    }
}
