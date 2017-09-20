package com.arseeds.ar.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.arseeds.ar.R;

/**
 * Created by Bert on 2017/9/20.
 */

public class ScanView extends FrameLayout {

    private ImageView iv_scan_line;
    private TranslateAnimation animation;

    public ScanView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ScanView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScanView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context mContext){
        View scan_view = View.inflate(mContext, R.layout.view_scan, this);
        iv_scan_line = (ImageView) scan_view.findViewById(R.id.iv_scan_line);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
    }

    public void startScan(){
        iv_scan_line.startAnimation(animation);
    }

    public void onPause(){
        if (iv_scan_line != null) {
            iv_scan_line.clearAnimation();
            iv_scan_line.setVisibility(View.GONE);
        }
    }

    public void onResume(){
        if (iv_scan_line != null) {
            iv_scan_line.startAnimation(animation);
        }
    }

}
