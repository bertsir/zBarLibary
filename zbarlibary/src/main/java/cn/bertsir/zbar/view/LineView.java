package cn.bertsir.zbar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.bertsir.zbar.R;

import static android.R.attr.width;

/**
 * Created by Bert on 2017/9/22.
 */

public class LineView extends View {

    private Paint paint;//声明画笔
    private Canvas canvas;//画布
    private int line_color = getResources().getColor(R.color.common_color);
    private Shader mShader;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint=new Paint();//创建一个画笔
        canvas=new Canvas();

        paint.setStyle(Paint.Style.FILL);//设置非填充
        paint.setStrokeWidth(10);//笔宽5像素
       // paint.setColor(line_color);//设置为红笔
        paint.setAntiAlias(true);//锯齿不显示
    }

    public void setLinecolor(int color){
        line_color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mShader = new LinearGradient(0,0,getMeasuredWidth(),0,new int[] {Color.TRANSPARENT,line_color, Color.TRANSPARENT},null,
                Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        canvas.drawLine(0, 0, width, 0, paint);
    }
}
