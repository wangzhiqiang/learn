package wangzq.cn.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author wzq <wangzhiqiang@17money.com>
 *         Date : 2017-03-23 15:36
 */

public class DynamicWaveView extends View {
    public DynamicWaveView(Context context) {
        super(context);
        init();
    }

    public DynamicWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DynamicWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private int mWaveHeight;//默认高度的 1/2

    private Path mPath;
    private Paint mPaint;

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0x40DC2E24);
    }

    //偏移量
    private int mOffset = 0;

    private int width=0;
    private int height=0;
    private int circle=0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        //TODO 画路径
        mPath.moveTo(mOffset,circle);

        int count=0;
        for(int i=0;i<=width;i++){
            if(i%circle == 0){
                mPath.quadTo(mOffset+circle*count,circle,mOffset+circle*(count+1),circle);
                count ++;
            }
        }
//        mPath.lineTo(count*circle,height);
//        mPath.lineTo(width,height);
//        mPath.lineTo(mOffset,height);
//        mPath.lineTo(mOffset,circle);
//        mPath.close();
//
        canvas.drawPath(mPath,mPaint);

        mOffset++;
        if (mOffset >= width) {
            mOffset = 0;
        }
        postInvalidateDelayed(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        circle = h/2;
    }
}
