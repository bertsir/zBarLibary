package cn.bertsir.zbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.bertsir.zbar.R;

/**
 * Created by Bert on 2017/9/22.
 */

public class CornerView extends View {

    private Paint paint;//声明画笔
    private Canvas canvas;//画布

    private static final String TAG = "CornerView";
    private int width = 0;
    private int height = 0;

    private int cornerColor;
    private int cornerWidth;
    private int cornerGravity;


    private static final int LEFT_TOP = 0;
    private static final int LEFT_BOTTOM = 1;
    private static final int RIGHT_TOP = 2;
    private static final int RIGHT_BOTTOM = 3;

    public CornerView(Context context) {
        super(context,null);
    }

    public CornerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerView);
        cornerColor = a.getColor(R.styleable.CornerView_corner_color, getResources().getColor(R.color.common_color));
        cornerWidth = (int) a.getDimension(R.styleable.CornerView_corner_width, 10);
        cornerGravity = a.getInt(R.styleable.CornerView_corner_gravity, 1);
        a.recycle();

        paint=new Paint();//创建一个画笔
        canvas=new Canvas();

        paint.setStyle(Paint.Style.FILL);//设置非填充
        paint.setStrokeWidth(cornerWidth);//笔宽5像素
        paint.setColor(cornerColor);//设置为红笔
        paint.setAntiAlias(true);//锯齿不显示
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (cornerGravity){
            case LEFT_TOP:
                canvas.drawLine(0, 0, width, 0, paint);
                canvas.drawLine(0, 0, 0, height, paint);
                break;
            case LEFT_BOTTOM:
                canvas.drawLine(0, 0, 0, height, paint);
                canvas.drawLine(0, height, width, height, paint);
                break;
            case RIGHT_TOP:
                canvas.drawLine(0, 0, width, 0, paint);
                canvas.drawLine(width, 0, width, height, paint);
                break;
            case RIGHT_BOTTOM:
                canvas.drawLine(width, 0, width, height, paint);
                canvas.drawLine(0, height, width, height, paint);
                break;
        }

    }


    public void setColor(int color){
        cornerColor = color;
        paint.setColor(cornerColor);
        invalidate();
    }

    public void setLineWidth(int dp){
        cornerWidth = dip2px(dp);
        paint.setStrokeWidth(cornerWidth);
        invalidate();
    }


    public int dip2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}
