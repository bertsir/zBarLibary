package cn.bertsir.zbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import cn.bertsir.zbar.R;

/**
 * Created by Bert on 2019/3/1.
 * Mail: bertsir@163.com
 */
public class VerticalSeekBar extends SeekBar {
    private static final String TAG = VerticalSeekBar.class.getSimpleName();

    public static final int ROTATION_ANGLE_CW_90 = 90;
    public static final int ROTATION_ANGLE_CW_270 = 270;

    private int mRotationAngle = ROTATION_ANGLE_CW_90;

    public VerticalSeekBar(Context context) {
        super(context);//注意是super 而不是调用其他构造函数
        initialize(context, null, 0, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekBar, defStyleAttr, defStyleRes);
            final int rotationAngle = a.getInteger(R.styleable.VerticalSeekBar_seekBarRotation, 0);
            if (isValidRotationAngle(rotationAngle)) {
                mRotationAngle = rotationAngle;
            }
            a.recycle();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        if (mRotationAngle == ROTATION_ANGLE_CW_270) {
            //从下到上
            c.rotate(270);
            c.translate(-getHeight(), 0);
        } else if (mRotationAngle == ROTATION_ANGLE_CW_90) {
            //从上到下
            c.rotate(90);
            c.translate(0, -getWidth());
        }

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (mRotationAngle == ROTATION_ANGLE_CW_270) {
                    //从下到上
                    setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                } else if (mRotationAngle == ROTATION_ANGLE_CW_90) {
                    //从上到下
                    setProgress((int) (getMax() * event.getY() / getHeight()));
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private static boolean isValidRotationAngle(int angle) {
        return (angle == ROTATION_ANGLE_CW_90 || angle == ROTATION_ANGLE_CW_270);
    }
}
