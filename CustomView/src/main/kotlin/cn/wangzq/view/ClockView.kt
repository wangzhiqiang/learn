package cn.wangzq.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.util.*


/**
 * @author wzq
 * @email wangzhiqiang@17money.com
 * @date : 2017-05-25 13:43
 */
class ClockView : View {

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

    private val MINUTES: Int = 60
    private val HOURS: Int = MINUTES * 60
    private val DAY: Int = 24 * HOURS

    private var mPaint: Paint = Paint()
    private val mTimer = Timer()
    private var mCenterX: Float = 0F
    private var mCenterY: Float = 0F
    private var mRadius: Float = 0F

    private var mLongMark = 45
    private var mShortMark = 30


    fun init() {
        mPaint.isAntiAlias = true
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(
                        { invalidate() })
            }
        }, 0, 500)
    }


    override fun onDraw(canvas: Canvas) {

        //中心点坐标
        mCenterX = (width / 2).toFloat()
        mCenterY = (height / 2).toFloat()
        //半径
        mRadius = (width / 3).toFloat()
//        //表盘
//        mPaint.style = Paint.Style.FILL_AND_STROKE
//        mPaint.color = Color.GRAY
//        canvas.drawCircle(mCenterX, mCenterY, mRadius - 1.5F , mPaint)


        //圆圈
        mPaint.strokeWidth = 3F
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint)


        //将坐标原点移到圆心处
        canvas.translate(mCenterX, mCenterY)
        //设置刻度线线宽
        mPaint.strokeWidth = 2F
        mPaint.textSize = 25F

        for (i in 0..59) {
            if (i % 5 == 0) {
                canvas.drawLine(0F, mLongMark - mRadius, 0F, -mRadius, mPaint)
            } else {
                canvas.drawLine(0F, mShortMark - mRadius, 0F, -mRadius, mPaint)
            }
            canvas.rotate(6F)
        }
        //画时间文字
        val textBound = Rect()
        mPaint.getTextBounds(12.toString() + "", 0, (12.toString() + "").length, textBound)
        for (i in 0..11) {
            val text = if (i == 0) "12" else i.toString() + ""
            val degree = (i * 30).toFloat()
            mPaint.getTextBounds(text, 0, text.length, textBound)
            canvas.rotate(degree)
            canvas.translate(0F, mLongMark + mPaint.textSize / 3 * 2 - mRadius)
            canvas.rotate(-degree)
            canvas.drawText(text, (-textBound.width() / 2).toFloat(), (textBound.height() / 2).toFloat(), mPaint)
            canvas.rotate(degree)
            canvas.translate(0F, mRadius - (mLongMark + mPaint.textSize / 3 * 2))
            canvas.rotate(-degree)
        }

        val d = Date()
        val hours = d.hours
        val minutes = d.minutes
        val seconds = d.seconds
//        //画时针 分针 秒针
        mPaint.color = 0xff0F5056.toInt()
        mPaint.strokeWidth = 4F
        //时针
        val currentHours = (hours * 60 * 60 + minutes * 60 + seconds).toFloat()
        val degreeHours = currentHours / DAY * 360
        canvas.rotate(degreeHours * 2)
        canvas.drawLine(0F, 0F, 0F, -mRadius / 4, mPaint)
        val pathHours = Path()
        pathHours.moveTo(0F, -mRadius / 4 - 3)
        pathHours.lineTo(2F, -mRadius / 4)
        pathHours.lineTo(-2F, -mRadius / 4)
        pathHours.close()
        canvas.drawPath(pathHours, mPaint)
        canvas.rotate(-degreeHours * 2)

        //分针
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 3F

        val currentMinutes = (minutes * 60 + seconds).toFloat()
        val degreeMinutes = currentMinutes / HOURS * 360

        canvas.rotate(degreeMinutes)
        canvas.drawLine(0F, 0F, 0F, -mRadius / 3, mPaint)
        val pathMinutes = Path()
        pathMinutes.moveTo(0F, -mRadius / 3 - 3)
        pathMinutes.lineTo(2F, -mRadius / 3)
        pathMinutes.lineTo(-2F, -mRadius / 3)
        pathMinutes.close()
        canvas.drawPath(pathMinutes, mPaint)
        canvas.rotate(-degreeMinutes)

        //秒
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = 2F

        val currentSeconds = seconds.toFloat()
        val degreeSeconds = currentSeconds / MINUTES * 360

        canvas.rotate(degreeSeconds)
        canvas.drawLine(0F, 0F, 0F, -mRadius / 2, mPaint)
        val pathSeconds = Path()
        pathSeconds.moveTo(0F, -mRadius / 2 - 3)
        pathSeconds.lineTo(2F, -mRadius / 2)
        pathSeconds.lineTo(-2F, -mRadius / 2)
        pathSeconds.close()
        canvas.drawPath(pathSeconds, mPaint)
        canvas.rotate(-degreeSeconds)

        //圆心
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(0F, 0F, 4F, mPaint)

        super.onDraw(canvas)
    }
}