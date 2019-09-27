package cn.bertsir.qrtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanLineView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_scan;
    private static final String TAG = "MainActivity";
    private ImageView iv_qr;
    private Button bt_make;
    private EditText et_qr_content;
    private EditText et_qr_title;
    private EditText et_qr_des;
    private CheckBox cb_show_title;
    private CheckBox cd_show_des;
    private CheckBox cb_show_ding;
    private CheckBox cb_show_custom_ding;
    private CheckBox cb_show_flash;
    private CheckBox cb_show_album;
    private CheckBox cb_only_center;
    private CheckBox cb_create_add_water;
    private CheckBox cb_crop_image;
    private CheckBox cb_show_zoom;
    private CheckBox cb_auto_zoom;
    private CheckBox cb_finger_zoom;
    private CheckBox cb_double_engine;
    private CheckBox cb_loop_scan;
    private CheckBox cb_auto_light;
    private RadioButton rb_qrcode;
    private RadioButton rb_bcode;
    private RadioButton rb_all;
    private RadioButton rb_screen_sx;
    private RadioButton rb_screen_hx;
    private RadioButton rb_screen_auto;
    private EditText et_loop_scan_time;
    private RadioButton rb_scanline_radar;
    private RadioButton rb_scanline_grid;
    private RadioButton rb_scanline_hybrid;
    private RadioButton rb_scanline_line;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_scan.setOnClickListener(this);
        iv_qr = (ImageView) findViewById(R.id.iv_qr);
        iv_qr.setOnClickListener(this);
        bt_make = (Button) findViewById(R.id.bt_make);
        bt_make.setOnClickListener(this);

        iv_qr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String s = null;
                try {
                    s = QRUtils.getInstance().decodeQRcode(iv_qr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onLongClickException: " + e.toString());
                }
                Toast.makeText(getApplicationContext(), "内容：" + s, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        et_qr_content = (EditText) findViewById(R.id.et_qr_content);
        et_qr_title = (EditText) findViewById(R.id.et_qr_title);
        et_qr_des = (EditText) findViewById(R.id.et_qr_des);
        cb_show_title = (CheckBox) findViewById(R.id.cb_show_title);
        cd_show_des = (CheckBox) findViewById(R.id.cd_show_des);
        cb_show_ding = (CheckBox) findViewById(R.id.cb_show_ding);
        cb_show_custom_ding = (CheckBox) findViewById(R.id.cb_show_custom_ding);
        cb_show_flash = (CheckBox) findViewById(R.id.cb_show_flash);
        cb_show_album = (CheckBox) findViewById(R.id.cb_show_album);
        cb_only_center = (CheckBox) findViewById(R.id.cb_only_center);
        cb_crop_image = (CheckBox) findViewById(R.id.cb_crop_image);
        cb_create_add_water = (CheckBox) findViewById(R.id.cb_create_add_water);
        cb_show_zoom = (CheckBox) findViewById(R.id.cb_show_zoom);
        cb_auto_zoom = (CheckBox) findViewById(R.id.cb_auto_zoom);
        cb_finger_zoom = (CheckBox) findViewById(R.id.cb_finger_zoom);
        cb_double_engine = (CheckBox) findViewById(R.id.cb_double_engine);
        cb_loop_scan = (CheckBox) findViewById(R.id.cb_loop_scan);
        rb_qrcode = (RadioButton) findViewById(R.id.rb_qrcode);
        rb_bcode = (RadioButton) findViewById(R.id.rb_bcode);
        rb_all = (RadioButton) findViewById(R.id.rb_all);
        rb_screen_hx = (RadioButton) findViewById(R.id.rb_screen_hx);
        rb_screen_sx = (RadioButton) findViewById(R.id.rb_screen_sx);
        rb_screen_auto = (RadioButton) findViewById(R.id.rb_screen_auto);
        et_loop_scan_time = (EditText) findViewById(R.id.et_loop_scan_time);

        rb_qrcode.setChecked(true);
        rb_screen_sx.setChecked(true);

        rb_scanline_radar = (RadioButton) findViewById(R.id.rb_scanline_radar);
        rb_scanline_radar.setOnClickListener(this);
        rb_scanline_grid = (RadioButton) findViewById(R.id.rb_scanline_grid);
        rb_scanline_grid.setOnClickListener(this);
        rb_scanline_hybrid = (RadioButton) findViewById(R.id.rb_scanline_hybrid);
        rb_scanline_hybrid.setOnClickListener(this);
        rb_scanline_line = (RadioButton) findViewById(R.id.rb_scanline_line);
        rb_scanline_line.setOnClickListener(this);
        cb_auto_light = (CheckBox) findViewById(R.id.cb_auto_light);
        cb_auto_light.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scan:
                start();
                break;
            case R.id.bt_make:
                Bitmap qrCode = null;
                if (cb_create_add_water.isChecked()) {
                    qrCode = QRUtils.getInstance().createQRCodeAddLogo(et_qr_content.getText().toString(),
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                } else {
                    qrCode = QRUtils.getInstance().createQRCode(et_qr_content.getText().toString());

                }
                iv_qr.setImageBitmap(qrCode);
                Toast.makeText(getApplicationContext(), "长按可识别", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void start() {
        int scan_type = 0;
        int scan_view_type = 0;
        int screen = 1;
        int line_style = ScanLineView.style_radar;
        if (rb_all.isChecked()) {
            scan_type = QrConfig.TYPE_ALL;
            scan_view_type = QrConfig.SCANVIEW_TYPE_QRCODE;
        } else if (rb_qrcode.isChecked()) {
            scan_type = QrConfig.TYPE_QRCODE;
            scan_view_type = QrConfig.SCANVIEW_TYPE_QRCODE;
        } else if (rb_bcode.isChecked()) {
            scan_type = QrConfig.TYPE_BARCODE;
            scan_view_type = QrConfig.SCANVIEW_TYPE_BARCODE;
        }
        if (rb_screen_auto.isChecked()) {
            screen = QrConfig.SCREEN_SENSOR;
        } else if (rb_screen_sx.isChecked()) {
            screen = QrConfig.SCREEN_PORTRAIT;
        } else if (rb_screen_hx.isChecked()) {
            screen = QrConfig.SCREEN_LANDSCAPE;
        }

        if (rb_scanline_radar.isChecked()) {
            line_style = ScanLineView.style_radar;
        } else if (rb_scanline_grid.isChecked()) {
            line_style = ScanLineView.style_gridding;
        } else if (rb_scanline_hybrid.isChecked()) {
            line_style = ScanLineView.style_hybrid;
        } else if (rb_scanline_line.isChecked()) {
            line_style = ScanLineView.style_line;
        }


        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText(et_qr_des.getText().toString())//扫描框下文字
                .setShowDes(cd_show_des.isChecked())//是否显示扫描框下面文字
                .setShowLight(cb_show_flash.isChecked())//显示手电筒按钮
                .setShowTitle(cb_show_title.isChecked())//显示Title
                .setShowAlbum(cb_show_album.isChecked())//显示从相册选择按钮
                .setNeedCrop(cb_crop_image.isChecked())//是否从相册选择后裁剪图片
                .setCornerColor(Color.parseColor("#E42E30"))//设置扫描框颜色
                .setLineColor(Color.parseColor("#E42E30"))//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(scan_type)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(scan_view_type)//设置扫描框类型（二维码还是条形码，默认为二维码）
                .setCustombarcodeformat(QrConfig.BARCODE_EAN13)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(cb_show_ding.isChecked())//是否扫描成功后bi~的声音
                .setDingPath(cb_show_custom_ding.isChecked() ? R.raw.test : R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(cb_only_center.isChecked())//是否只识别框中内容(默认为全屏识别)
                .setTitleText(et_qr_title.getText().toString())//设置Tilte文字
                .setTitleBackgroudColor(Color.parseColor("#262020"))//设置状态栏颜色
                .setTitleTextColor(Color.WHITE)//设置Title文字颜色
                .setShowZoom(cb_show_zoom.isChecked())//是否开始滑块的缩放
                .setAutoZoom(cb_auto_zoom.isChecked())//是否开启自动缩放(实验性功能，不建议使用)
                .setFingerZoom(cb_finger_zoom.isChecked())//是否开始双指缩放
                .setDoubleEngine(cb_double_engine.isChecked())//是否开启双引擎识别(仅对识别二维码有效，并且开启后只识别框内功能将失效)
                .setScreenOrientation(screen)//设置屏幕方式
                .setOpenAlbumText("选择要识别的图片")//打开相册的文字
                .setLooperScan(cb_loop_scan.isChecked())//是否连续扫描二维码
                .setLooperWaitTime(Integer.parseInt(et_loop_scan_time.getText().toString()) * 1000)//连续扫描间隔时间
                .setScanLineStyle(line_style)//扫描线样式
                .setAutoLight(cb_auto_light.isChecked())//自动灯光
                .create();
        QrManager.getInstance().init(qrConfig).startScan(MainActivity.this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(ScanResult result) {
                Log.e(TAG, "onScanSuccess: " + result);
                Toast.makeText(getApplicationContext(), "内容：" + result.getContent()
                        + "  类型：" + result.getType(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
