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

    public boolean isPlay_sound() {
        return play_sound;
    }

    public boolean play_sound = true;

    public String title_text = "扫描二维码";
    public String des_text = "(识别二维码)";

    public int line_speed = LINE_FAST;
    public int corner_width = 10;

    public static final int TYPE_QRCODE = 1;
    public  static final int TYPE_BARCODE = 2;



    public int getScan_type() {
        return scan_type;
    }

    public int scan_type = TYPE_QRCODE;



    public final static int REQUEST_CAMERA = 99;
    public final static String EXTRA_THIS_CONFIG = "extra_this_config";

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

        public QrConfig create(){
            return watcher;
        }
    }




}
