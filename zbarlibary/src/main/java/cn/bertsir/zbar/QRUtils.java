package cn.bertsir.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import cn.bertsir.zbar.Qr.Config;
import cn.bertsir.zbar.Qr.Image;
import cn.bertsir.zbar.Qr.ImageScanner;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.Qr.SymbolSet;

/**
 * Created by Bert on 2017/9/20.
 */

public class QRUtils {

    private static QRUtils instance;
    private Bitmap scanBitmap;


    public static QRUtils getInstance() {
        if (instance == null)
            instance = new QRUtils();
        return instance;
    }


    /**
     * 识别本地二维码
     *
     * @param url
     * @return
     */
    public String decodeQRcode(String url) throws Exception {
        Bitmap qrbmp = BitmapFactory.decodeFile(url);
        if (qrbmp != null) {
            return decodeQRcode(qrbmp);
        } else {
            return "";
        }

    }

    public String decodeQRcode(ImageView iv) throws Exception {
        Bitmap qrbmp = ((BitmapDrawable) (iv).getDrawable()).getBitmap();
        if (qrbmp != null) {
            return decodeQRcode(qrbmp);
        } else {
            return "";
        }
    }

    public String decodeQRcode(Bitmap barcodeBmp) throws Exception {
        int    width      = barcodeBmp.getWidth();
        int    height     = barcodeBmp.getHeight();
        int[]  pixels     = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader       = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int          result       = reader.scanImage(barcode.convert("Y800"));
        String       qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }

    /**
     * 识别本地条形码
     *
     * @param url
     * @return
     */
    public String decodeBarcode(String url) throws Exception {
        Bitmap qrbmp = BitmapFactory.decodeFile(url);
        if (qrbmp != null) {
            return decodeBarcode(qrbmp);
        } else {
            return "";
        }

    }

    public String decodeBarcode(ImageView iv) throws Exception {
        Bitmap qrbmp = ((BitmapDrawable) (iv).getDrawable()).getBitmap();
        if (qrbmp != null) {
            return decodeBarcode(qrbmp);
        } else {
            return "";
        }
    }

    public String decodeBarcode(Bitmap barcodeBmp) throws Exception {
        int    width      = barcodeBmp.getWidth();
        int    height     = barcodeBmp.getHeight();
        int[]  pixels     = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader       = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        reader.setConfig(Symbol.CODE39, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN8, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        int          result       = reader.scanImage(barcode.convert("Y800"));
        String       qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }



    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public Bitmap createQRCode(String content) {
        return createQRCode(content, 300, 300);
    }

    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public Bitmap createQRCode(String content, int width, int height) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);//这里调整二维码的容错率
            hints.put(EncodeHintType.MARGIN, 1);   //设置白边取值1-4，值越大白边越大
            result = multiFormatWriter.encode(new String(content.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat
                    .QR_CODE, width, height, hints);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 生成带logo的二维码
     *
     * @param content
     * @param logo
     * @return
     */
    public Bitmap createQRCodeAddLogo(String content, Bitmap logo) {
        Bitmap qrCode = createQRCode(content);
        int qrheight = qrCode.getHeight();
        int qrwidth = qrCode.getWidth();
        int waterWidth = (int) (qrwidth * 0.3);//0.3为logo占二维码大小的倍数 建议不要过大，否则二维码失效
        float scale = waterWidth / (float) logo.getWidth();
        Bitmap waterQrcode = createWaterMaskCenter(qrCode, zoomImg(logo, scale));
        return waterQrcode;
    }


    public Bitmap createQRCodeAddLogo(String content, int width, int height, Bitmap logo) {
        Bitmap qrCode = createQRCode(content, width, height);
        int qrheight = qrCode.getHeight();
        int qrwidth = qrCode.getWidth();
        int waterWidth = (int) (qrwidth * 0.3);//0.3为logo占二维码大小的倍数 建议不要过大，否则二维码失效
        float scale = waterWidth / (float) logo.getWidth();
        Bitmap waterQrcode = createWaterMaskCenter(qrCode, zoomImg(logo, scale));
        return waterQrcode;
    }

    /**
     * 生成条形码
     *
     * @param context
     * @param contents
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    @Deprecated
    public Bitmap createBarcode(Context context, String contents, int desiredWidth, int desiredHeight) {
        if (TextUtils.isEmpty(contents)) {
            throw new NullPointerException("contents not be null");
        }
        if (desiredWidth == 0 || desiredHeight == 0) {
            throw new NullPointerException("desiredWidth or desiredHeight not be null");
        }
        Bitmap resultBitmap;
        /**
         * 条形码的编码类型
         */
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        resultBitmap = encodeAsBitmap(contents, barcodeFormat,
                desiredWidth, desiredHeight);
        return resultBitmap;
    }

    /**
     * 生成条形码
     *
     * @param context
     * @param contents
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public Bitmap createBarCodeWithText(Context context, String contents, int desiredWidth,
                                        int desiredHeight) {
        return createBarCodeWithText(context, contents, desiredWidth, desiredHeight, null);
    }

    public Bitmap createBarCodeWithText(Context context, String contents, int desiredWidth,
                                        int desiredHeight, TextViewConfig config) {
        if (TextUtils.isEmpty(contents)) {
            throw new NullPointerException("contents not be null");
        }
        if (desiredWidth == 0 || desiredHeight == 0) {
            throw new NullPointerException("desiredWidth or desiredHeight not be null");
        }
        Bitmap resultBitmap;

        /**
         * 条形码的编码类型
         */
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
                desiredWidth, desiredHeight);

        Bitmap codeBitmap = createCodeBitmap(contents, barcodeBitmap.getWidth(),
                barcodeBitmap.getHeight(), context, config);

        resultBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
                0, desiredHeight));
        return resultBitmap;
    }

    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }


    private Bitmap createCodeBitmap(String contents, int width, int height, Context context,
                                    TextViewConfig config) {
        if (config == null) {
            config = new TextViewConfig();
        }
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setTextSize(config.size == 0 ? tv.getTextSize() : config.size);
        tv.setHeight(height);
        tv.setGravity(config.gravity);
        tv.setMaxLines(config.maxLines);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(config.color);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        return tv.getDrawingCache();
    }

    public static class TextViewConfig {

        private int gravity = Gravity.CENTER;
        private int maxLines = 1;
        private @ColorInt
        int color = Color.BLACK;
        private float size;

        public TextViewConfig() {
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public void setMaxLines(int maxLines) {
            this.maxLines = maxLines;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setSize(float size) {
            this.size = size;
        }
    }

    /**
     * 将两个Bitmap合并成一个
     *
     * @param first
     * @param second
     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    private Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }

        int width = Math.max(first.getWidth(), second.getWidth());
        Bitmap newBitmap = Bitmap.createBitmap(
                width,
                first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    private Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    private Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newb;
    }

    /**
     * 缩放Bitmap
     * @param bm
     * @param f
     * @return
     */
    private Bitmap zoomImg(Bitmap bm, float f) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = f;
        float scaleHeight = f;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
