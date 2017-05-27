package cn.wangzq.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.lang.Math

/**
 * @author wzq
 * @email wangzhiqiang@17money.com
 * @date : 2017-05-27 10:25
 */
class CircleLoading : View {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }


    private var mPaint: Paint = Paint()
    private val mValueAnimator: ValueAnimator = ValueAnimator.ofFloat(-1F, 1F)

    fun init() {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 20F
        mPaint.isAntiAlias = true

        mValueAnimator.duration = 3000L
        mValueAnimator.repeatCount = -1
        mValueAnimator.repeatMode = ValueAnimator.REVERSE
        mValueAnimator.addUpdateListener {
            invalidate()
        }
        mValueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {

        //中心点
        var x1 = (width / 2).toFloat()
        var y1 = (height / 2).toFloat()

        //画布最大半径
        var r = (width - paddingLeft - paddingRight - mPaint.strokeWidth) / 2

        //移动坐标原点到中心
        canvas.translate(x1, y1)

        //圆公式
        //r^2=x^2+y^2

        mPaint.strokeWidth = 1f
        mPaint.color=Color.BLUE

        canvas.drawCircle(0f,0f,r,mPaint)

        mPaint.strokeWidth = 10f
        mPaint.color=Color.RED

        var t = mValueAnimator.animatedValue as Float
        var x = t * r
//        for (i in 0..5 - 1) {

//            if (t < 0) {
//                x += Math.abs(t + 0.05f * i) * r
//            } else {
//                x -= (t - 0.05f * i) * r
//            }

            var y = Math.sqrt((r * r - x * x).toDouble()).toFloat()

            canvas.drawPoint(x, y, mPaint)
            canvas.drawPoint(x, -y, mPaint)

//        }


        super.onDraw(canvas)
    }
}