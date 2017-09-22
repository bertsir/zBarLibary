package cn.bertsir.qrtest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bertsir.zbar.QRUtils;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt;
    private static final String TAG = "MainActivity";
    private ImageView iv_qr;
    private Button bt_make;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    private void initView() {
        bt = (Button) findViewById(R.id.bt);

        bt.setOnClickListener(this);
        iv_qr = (ImageView) findViewById(R.id.iv_qr);
        iv_qr.setOnClickListener(this);
        bt_make = (Button) findViewById(R.id.bt_make);
        bt_make.setOnClickListener(this);

        iv_qr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String s = QRUtils.getInstance().decodeQRcode(iv_qr);
                Toast.makeText(getApplicationContext(), "内容：" + s, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                start();
                break;
            case R.id.iv_qr:

                break;
            case R.id.bt_make:
                Bitmap qrCode = QRUtils.getInstance().createQRCode("www.qq.com");
                iv_qr.setImageBitmap(qrCode);
                Toast.makeText(getApplicationContext(), "长按可识别", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void start(){
        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText("(识别二维码)")
                .setShowLight(true)
                .setShowTitle(true)
                .setCornerColor(Color.WHITE)
                .setLineColor(Color.WHITE)
                .setLineSpeed(QrConfig.LINE_MEDIUM)
                .setScanType(QrConfig.TYPE_QRCODE)
                .setPlaySound(false)
                .setTitleText("扫描二维码")
                .create();
        QrManager.getInstance().init(qrConfig).startScan(MainActivity.this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
