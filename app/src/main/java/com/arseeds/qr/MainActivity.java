package com.arseeds.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arseeds.ar.FunctionConfig;
import com.arseeds.ar.QRActivity;
import com.arseeds.ar.QRUtils;


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
                Toast.makeText(getApplicationContext(),"内容："+s,Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                startActivityForResult(new Intent(getApplicationContext(), QRActivity.class), FunctionConfig.REQUEST_CAMERA);
                break;
            case R.id.iv_qr:

                break;
            case R.id.bt_make:
                Bitmap qrCode = QRUtils.getInstance().createQRCode("www.qq.com");
                iv_qr.setImageBitmap(qrCode);
                Toast.makeText(getApplicationContext(),"长按可识别",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FunctionConfig.REQUEST_CAMERA) {
                Log.e(TAG, "onActivityResult: " + data.getStringExtra("QRcontent"));
                Toast.makeText(getApplicationContext(), data.getStringExtra("QRcontent"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
