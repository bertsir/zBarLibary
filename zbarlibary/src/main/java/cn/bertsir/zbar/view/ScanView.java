package cn.bertsir.zbar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.R;


/**
 * Created by Bert on 2017/9/20.
 */

public class ScanView extends FrameLayout {

    private ScanLineView iv_scan_line;
    private FrameLayout fl_scan;
    private int CURRENT_TYEP = 1;
    private CornerView cnv_left_top;
    private CornerView cnv_left_bottom;
    private CornerView cnv_right_top;
    private CornerView cnv_right_bottom;
    private ArrayList<CornerView> cornerViews;
    private int line_speed = 3000;

    public ScanView(Context context) {
        super(context);
        initView(context);
    }

    public ScanView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScanView(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context mContext){
        View scan_view = View.inflate(mContext, R.layout.view_scan, this);

        cnv_left_top = (CornerView) scan_view.findViewById(R.id.cnv_left_top);
        cnv_left_bottom = (CornerView) scan_view.findViewById(R.id.cnv_left_bottom);
        cnv_right_top = (CornerView) scan_view.findViewById(R.id.cnv_right_top);
        cnv_right_bottom = (CornerView) scan_view.findViewById(R.id.cnv_right_bottom);

        cornerViews = new ArrayList<>();
        cornerViews.add(cnv_left_top);
        cornerViews.add(cnv_left_bottom);
        cornerViews.add(cnv_right_top);
        cornerViews.add(cnv_right_bottom);

        iv_scan_line = (ScanLineView) scan_view.findViewById(R.id.iv_scan_line);

        fl_scan = (FrameLayout) scan_view.findViewById(R.id.fl_scan);
        getViewWidthHeight();

    }

    /**
     * 设置扫描速度
     * @param speed
     */
    public void setLineSpeed(int speed){
        iv_scan_line.setScanAnimatorDuration(speed);
    }


    /**
     * 设置扫描样式
     */
    public void setScanLineStyle(int style){
        iv_scan_line.setScanStyle(style);
    }


    public void setType(int type){
        CURRENT_TYEP = type;
        LinearLayout.LayoutParams fl_params = (LinearLayout.LayoutParams) fl_scan.getLayoutParams();
        if(CURRENT_TYEP == QrConfig.SCANVIEW_TYPE_QRCODE){
            fl_params.width = dip2px(200);
            fl_params.height = dip2px(200);
        }else if(CURRENT_TYEP == QrConfig.SCANVIEW_TYPE_BARCODE){
            fl_params.width = dip2px(300);
            fl_params.height = dip2px(100);
        }
        fl_scan.setLayoutParams(fl_params);
    }

    public void setCornerColor(int color){
        for (int i = 0; i < cornerViews.size(); i++) {
            cornerViews.get(i).setColor(color);
        }
    }

    public void setCornerWidth(int dp){
        for (int i = 0; i < cornerViews.size(); i++) {
            cornerViews.get(i).setLineWidth(dp);
        }
    }

    public void setLineColor(int color){
        iv_scan_line.setScancolor(color);
    }

    public int dip2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public void getViewWidthHeight(){
        fl_scan.post(new Runnable() {
            @Override
            public void run() {
                Symbol.cropWidth = fl_scan.getWidth();
                Symbol.cropHeight = fl_scan.getHeight();
            }
        });
    }



}
