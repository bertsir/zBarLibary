package cn.bertsir.zbar;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Bert on 2017/9/22.
 */

public class QrConfig implements Serializable {


    public static final int LINE_FAST = 1000;
    public static final int LINE_MEDIUM = 3000;
    public static final int LINE_SLOW = 5000;


    public  int CORNER_COLOR = Color.parseColor("#ff5f00");
    public  int LINE_COLOR = Color.parseColor("#ff5f00");

    public  int TITLE_BACKGROUND_COLOR = Color.parseColor("#ff5f00");
    public  int TITLE_TEXT_COLOR = Color.parseColor("#ffffff");

    public boolean show_title = true;
    public boolean show_light = true;
    public boolean show_album = true;
    public boolean show_des = true;



    public boolean only_center = false;



    public boolean play_sound = true;

    public String title_text = "扫描二维码";
    public String des_text = "(识别二维码)";

    public int line_speed = LINE_FAST;
    public int corner_width = 10;


    public static  int ding_path = R.raw.qrcode;//默认声音

    public int custombarcodeformat = -1;

    public static final int TYPE_QRCODE = 1;//扫描二维码
    public  static final int TYPE_BARCODE = 2;//扫描条形码（UPCA）
    public  static final int TYPE_ALL = 3;//扫描全部类型码
    public  static final int TYPE_CUSTOM = 4;//扫描用户定义类型码

    public static final int SCANVIEW_TYPE_QRCODE = 1;//二维码框
    public static final int SCANVIEW_TYPE_BARCODE = 2;//条形码框




    public int scan_type = TYPE_QRCODE;//默认只扫描二维码
    public int scan_view_type = SCANVIEW_TYPE_QRCODE;//默认为二维码扫描框
    public final static int REQUEST_CAMERA = 99;
    public final static String EXTRA_THIS_CONFIG = "extra_this_config";

    /**
     * EAN-8.
     */
    public static final int BARCODE_EAN8 = 8;
    /**
     * UPC-E.
     */
    public static final int BARCODE_UPCE = 9;
    /**
     * ISBN-10 (from EAN-13).
     */
    public static final int BARCODE_ISBN10 = 10;
    /**
     * UPC-A.
     */
    public static final int BARCODE_UPCA = 12;
    /**
     * EAN-13.
     */
    public static final int BARCODE_EAN13 = 13;
    /**
     * ISBN-13 (from EAN-13).
     */
    public static final int BARCODE_ISBN13 = 14;
    /**
     * Interleaved 2 of 5.
     */
    public static final int BARCODE_I25 = 25;
    /**
     * DataBar (RSS-14).
     */
    public static final int BARCODE_DATABAR = 34;
    /**
     * DataBar Expanded.
     */
    public static final int BARCODE_DATABAR_EXP = 35;
    /**
     * Codabar.
     */
    public static final int BARCODE_CODABAR = 38;
    /**
     * Code 39.
     */
    public static final int BARCODE_CODE39 = 39;
    /**
     * PDF417.
     */
    public static final int BARCODE_PDF417 = 57;

    /**
     * Code 93.
     */
    public static final int BARCODE_CODE93 = 93;
    /**
     * Code 128.
     */
    public static final int BARCODE_CODE128 = 128;



    public int getScan_type() {
        return scan_type;
    }

    public boolean isPlay_sound() {
        return play_sound;
    }

    public int getCORNER_COLOR() {
        return CORNER_COLOR;
    }

    public int getLINE_COLOR() {
        return LINE_COLOR;
    }

    public int getTITLE_BACKGROUND_COLOR() {
        return TITLE_BACKGROUND_COLOR;
    }

    public int getTITLE_TEXT_COLOR() {
        return TITLE_TEXT_COLOR;
    }

    public boolean isShow_title() {
        return show_title;
    }

    public boolean isShow_light() {
        return show_light;
    }

    public boolean isShow_album() {
        return show_album;
    }

    public boolean isShow_des() {
        return show_des;
    }

    public String getTitle_text() {
        return title_text;
    }

    public String getDes_text() {
        return des_text;
    }

    public int getLine_speed() {
        return line_speed;
    }

    public int getCorner_width() {
        return corner_width;
    }

    public int getCustombarcodeformat() {
        return custombarcodeformat;
    }

    public int getScan_view_type() {
        return scan_view_type;
    }

    public boolean isOnly_center() {
        return only_center;
    }

    public static int getDing_path() {
        return ding_path;
    }



    public static class Builder{
        private QrConfig watcher;

        public Builder(){
            watcher = new QrConfig();
        }

        public Builder setLineSpeed(int speed) {
            watcher.line_speed = speed;
            return this;
        }

        public Builder setLineColor(int color){
            watcher.LINE_COLOR = color;
            return this;
        }

        public Builder setCornerColor(int color){
            watcher.CORNER_COLOR = color;
            return this;
        }

        public Builder setCornerWidth(int dp){
            watcher.corner_width = dp;
            return this;
        }

        public Builder setDesText(String text){
            watcher.des_text = text;
            return this;
        }

        public Builder setTitleText(String text){
            watcher.title_text = text;
            return this;
        }

        public Builder setShowTitle(boolean show){
            watcher.show_title = show;
            return this;
        }
        public Builder setShowLight(boolean show){
            watcher.show_light = show;
            return this;
        }
        public Builder setShowAlbum(boolean show){
            watcher.show_album = show;
            return this;
        }

        public Builder setShowDes(boolean show){
            watcher.show_des = show;
            return this;
        }

        public Builder setTitleBackgroudColor(int color){
            watcher.TITLE_BACKGROUND_COLOR = color;
            return this;
        }

        public Builder setTitleTextColor(int color){
            watcher.TITLE_TEXT_COLOR = color;
            return this;
        }

        public Builder setScanType(int type){
            watcher.scan_type = type;
            return this;
        }

        public Builder setPlaySound(boolean play){
            watcher.play_sound = play;
            return this;
        }

        public Builder setCustombarcodeformat(int format){
            watcher.custombarcodeformat = format;
            return this;
        }

        public Builder setScanViewType(int type){
            watcher.scan_view_type = type;
            return this;
        }

        public Builder setIsOnlyCenter(boolean isOnlyCenter){
            watcher.only_center = isOnlyCenter;
            return this;
        }

        public Builder setDingPath(int ding){
            watcher.ding_path = ding;
            return this;
        }

        public QrConfig create(){
            return watcher;
        }
    }

}
