package cn.bertsir.zbar.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import cn.bertsir.zbar.R;

/**
 * Created by Bert on 2019-09-16.
 * Mail: bertsir@163.com
 */
public class ScanLineView extends View {

    private static final String TAG = "ScanView";

    public static final int style_gridding = 0;//扫描区域的样式
    public static final int style_radar = 1;
    public static final int style_hybrid = 2;
    public static final int style_line = 3;


    private Rect mFrame;//最佳扫描区域的Rect

    private Paint mScanPaint_Gridding;//网格样式画笔
    private Paint mScanPaint_Radio;//雷达样式画笔
    private Paint mScanPaint_Line;//线条样式画笔

    private Path mBoundaryLinePath;//边框path
    private Path mGriddingPath;//网格样式的path

    private LinearGradient mLinearGradient_Radar;//雷达样式的画笔shader
    private LinearGradient mLinearGradient_Gridding;//网格画笔的shader
    private LinearGradient mLinearGradient_line;
    private float mGriddingLineWidth = 2;//网格线的线宽，单位pix
    private int mGriddingDensity = 40;//网格样式的，网格密度，值越大越密集


    private float mCornerLineLen = 50f;//根据比例计算的边框长度，从四角定点向临近的定点画出的长度

    private Matrix mScanMatrix;//变换矩阵，用来实现动画效果
    private ValueAnimator mValueAnimator;//值动画，用来变换矩阵操作

    private int mScanAnimatorDuration = 1800;//值动画的时长
    private int mScancolor;//扫描颜色

    private int mScanStyle = style_gridding;//网格 0：网格，1：纵向雷达 2:综合 3:线
    private float animatedValue;


    public ScanLineView(Context context) {
        this(context, null);
    }

    // This constructor is used when the class is built from an XML resource.
    public ScanLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ScanLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize these once for performance rather than calling them every time in onDraw().
        mScanPaint_Gridding = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScanPaint_Gridding.setStyle(Paint.Style.STROKE);
        mScanPaint_Gridding.setStrokeWidth(mGriddingLineWidth);

        mScanPaint_Radio = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScanPaint_Radio.setStyle(Paint.Style.FILL);
        Resources resources = getResources();
        mScancolor = resources.getColor(R.color.common_color);


        mScanPaint_Line=new Paint();//创建一个画笔
        mScanPaint_Line.setStyle(Paint.Style.FILL);//设置非填充
        mScanPaint_Line.setStrokeWidth(10);//笔宽5像素
        mScanPaint_Line.setAntiAlias(true);//锯齿不显示

        //变换矩阵，用来处理扫描的上下扫描效果
        mScanMatrix = new Matrix();
        mScanMatrix.setTranslate(0, 30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mFrame = new Rect(left,top,right,bottom);

        initBoundaryAndAnimator();
    }

    private void initBoundaryAndAnimator() {
        if (mBoundaryLinePath == null) {
            mBoundaryLinePath = new Path();
            mBoundaryLinePath.moveTo(mFrame.left, mFrame.top + mCornerLineLen);
            mBoundaryLinePath.lineTo(mFrame.left, mFrame.top);
            mBoundaryLinePath.lineTo(mFrame.left + mCornerLineLen, mFrame.top);
            mBoundaryLinePath.moveTo(mFrame.right - mCornerLineLen, mFrame.top);
            mBoundaryLinePath.lineTo(mFrame.right, mFrame.top);
            mBoundaryLinePath.lineTo(mFrame.right, mFrame.top + mCornerLineLen);
            mBoundaryLinePath.moveTo(mFrame.right, mFrame.bottom - mCornerLineLen);
            mBoundaryLinePath.lineTo(mFrame.right, mFrame.bottom);
            mBoundaryLinePath.lineTo(mFrame.right - mCornerLineLen, mFrame.bottom);
            mBoundaryLinePath.moveTo(mFrame.left + mCornerLineLen, mFrame.bottom);
            mBoundaryLinePath.lineTo(mFrame.left, mFrame.bottom);
            mBoundaryLinePath.lineTo(mFrame.left, mFrame.bottom - mCornerLineLen);
        }

        if (mValueAnimator == null) {
            initScanValueAnim(mFrame.height());
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (mFrame == null||mBoundaryLinePath==null) {
            return;
        }
        switch (mScanStyle) {
            case style_gridding:
                initGriddingPathAndStyle();
                canvas.drawPath(mGriddingPath, mScanPaint_Gridding);
                break;
            case style_radar:
                initRadarStyle();
                canvas.drawRect(mFrame, mScanPaint_Radio);
                break;
            case style_line:
                initLineStyle();
                canvas.drawLine(0,(mFrame.height()- Math.abs(animatedValue)),getMeasuredWidth(),
                        (mFrame.height()- Math.abs(animatedValue)), mScanPaint_Line);
                break;
            case style_hybrid:
            default:
                initGriddingPathAndStyle();
                initRadarStyle();
                canvas.drawPath(mGriddingPath, mScanPaint_Gridding);
                canvas.drawRect(mFrame, mScanPaint_Radio);
                break;

        }

    }

    private void initRadarStyle() {
        if (mLinearGradient_Radar == null) {
            mLinearGradient_Radar = new LinearGradient(0, mFrame.top, 0, mFrame.bottom + 0.01f * mFrame.height(),
                    new int[]{Color.TRANSPARENT, Color.TRANSPARENT, mScancolor, Color.TRANSPARENT}, new float[]{0, 0.85f, 0.99f, 1f}, LinearGradient.TileMode.CLAMP);
            mLinearGradient_Radar.setLocalMatrix(mScanMatrix);
            mScanPaint_Radio.setShader(mLinearGradient_Radar);
        }
    }

    private void initLineStyle() {
        if (mLinearGradient_line == null) {
            String line_colors = String.valueOf(Integer.toHexString(mScancolor));
            line_colors = line_colors.substring(line_colors.length() - 6, line_colors.length() - 0);
            mLinearGradient_line = new LinearGradient(0,0,getMeasuredWidth(),0,new int[] {Color.parseColor("#00"+line_colors),
                    mScancolor, Color.parseColor("#00"+line_colors),},null, Shader.TileMode.CLAMP);
            mLinearGradient_line.setLocalMatrix(mScanMatrix);
            mScanPaint_Line.setShader(mLinearGradient_line);
        }
    }

    private void initGriddingPathAndStyle() {
        if (mGriddingPath == null) {
            mGriddingPath = new Path();
            float wUnit = mFrame.width() / (mGriddingDensity + 0f);
            float hUnit = mFrame.height() / (mGriddingDensity + 0f);
            for (int i = 0; i <= mGriddingDensity; i++) {
                mGriddingPath.moveTo(mFrame.left + i * wUnit, mFrame.top);
                mGriddingPath.lineTo(mFrame.left + i * wUnit, mFrame.bottom);
            }
            for (int i = 0; i <= mGriddingDensity; i++) {
                mGriddingPath.moveTo(mFrame.left, mFrame.top + i * hUnit);
                mGriddingPath.lineTo(mFrame.right, mFrame.top + i * hUnit);
            }
        }
        if (mLinearGradient_Gridding == null) {
            mLinearGradient_Gridding = new LinearGradient(0, mFrame.top, 0, mFrame.bottom + 0.01f * mFrame.height(), new int[]{Color.TRANSPARENT, Color.TRANSPARENT, mScancolor, Color.TRANSPARENT}, new float[]{0, 0.5f, 0.99f, 1f}, LinearGradient.TileMode.CLAMP);
            mLinearGradient_Gridding.setLocalMatrix(mScanMatrix);
            mScanPaint_Gridding.setShader(mLinearGradient_Gridding);

        }
    }

    public void initScanValueAnim(int height) {
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(mScanAnimatorDuration);
        mValueAnimator.setFloatValues(-height, 0);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setRepeatCount(Animation.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {



            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mLinearGradient_Gridding == null){
                    initGriddingPathAndStyle();
                }
                if(mLinearGradient_Radar == null){
                    initRadarStyle();
                }

                if(mLinearGradient_line == null){
                    initLineStyle();
                }

                if (mScanMatrix != null ) {
                    animatedValue = (float) animation.getAnimatedValue();
                    mScanMatrix.setTranslate(0, animatedValue);
                    mLinearGradient_Gridding.setLocalMatrix(mScanMatrix);
                    mLinearGradient_Radar.setLocalMatrix(mScanMatrix);
                    mLinearGradient_line.setLocalMatrix(mScanMatrix);
                    //mScanPaint.setShader(mLinearGradient); //不是必须的设置到shader即可
                    invalidate();
                }
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    //设定扫描的颜色
    public void setScancolor(int colorValue) {
        this.mScancolor = colorValue;
    }

    public void setScanAnimatorDuration(int duration) {
        this.mScanAnimatorDuration = duration;
    }


    /*
     * @description 扫描区域的样式
     * @scanStyle
     *
     * */
    public void setScanStyle(int scanStyle) {
        this.mScanStyle = scanStyle;
    }

    /*
     * 扫描区域网格的样式
     *  @params strokeWidth：网格的线宽
     * @params density：网格的密度
     * */
    public void setScanGriddingStyle(float strokeWidh, int density) {
        this.mGriddingLineWidth = strokeWidh;
        this.mGriddingDensity = density;
    }
}

