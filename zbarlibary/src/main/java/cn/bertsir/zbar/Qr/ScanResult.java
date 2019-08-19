package cn.bertsir.zbar.Qr;

/**
 * Created by Bert on 2019-08-19.
 * Mail: bertsir@163.com
 */
public class ScanResult {

    public String content;
    public int type;

    public static final int CODE_QR = 1;//二维码
    public static final int CODE_BAR = 2;//条形码

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
