package cn.wangzq.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.util.TypedValue


/**
 * @author wzq
 * @email wangzhiqiang@17money.com
 * @date : 2017-05-25 16:16
 */

class Win10LoadingView : View {

    private var mPaint: Paint = Paint()
    private var mPath: Path = Path()
    private val mValueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private val mPathMeasure: PathMeasure = PathMeasure()


    //圆点数量  MAX 20
    private var mPointCount: Int = 5
    //圆点大小 直径
    private var mPointSize: Float = dip2px(4f)
    //圆点颜色
    @ColorInt
    private var mPointColor: Int = Color.RED
    //转一圈的时间
    private var mDuration: Float = 3000f
    //起始位置
    private var mRotate: Float = -90f
    //旋转方向
    private var mDirection: Path.Direction = Path.Direction.CW

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {

        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        mPointColor =  value.data

        val a = context.obtainStyledAttributes(attrs, R.styleable.win10)

        mPointCount = a.getInt(R.styleable.win10_count, mPointCount)

        mPointCount = Math.min(Math.abs(mPointCount), 20)

        mPointSize = a.getDimension(R.styleable.win10_size, mPointSize)
        mPointColor = a.getColor(R.styleable.win10_color, mPointColor)
        mDuration = a.getFloat(R.styleable.win10_duration, mDuration)
        mRotate = a.getFloat(R.styleable.win10_rotate, mRotate)

        var d = a.getInt(R.styleable.win10_direction, 0)
        if (d == 1) {
            mDirection = Path.Direction.CCW
        }
        a.recycle()

        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    //保障view的宽高一样
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var spec = Math.max(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(spec, spec)
    }

    fun init() {
        mPaint.color = mPointColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = mPointSize
        mPaint.isAntiAlias = true

        mValueAnimator.duration = mDuration.toLong()
        mValueAnimator.repeatCount = -1
        mValueAnimator.addUpdateListener { animation ->
            t = animation.animatedValue as Float
            invalidate()
        }
        mValueAnimator.start()

    }

    private var t = 0F
    val dst = Path()

    override fun onDraw(canvas: Canvas) {

        if (isInEditMode) {
            t = 0.05f * mPointCount
        }

        dst.rewind()

        //TODO 移动位置,不用每次计算

        var centerX: Float = (width / 2).toFloat()
        var centerY: Float = (height / 2).toFloat()
        var diameter: Float = (width - paddingLeft - paddingRight - mPointSize) / 2

        mPath.addCircle(0f, 0f, diameter, mDirection)
        mPathMeasure.setPath(mPath, false)

        canvas.translate(centerX, centerY)
        canvas.rotate(mRotate)

        if (t >= 0.99f) {
            canvas.drawPoint(diameter, 0f, mPaint)
        }
        var x: Float
        var s: Float
        var y: Float

        for (index in 0..mPointCount - 1) {

            x = t - index * 0.05f * (1f - t)
            s = mPathMeasure.length
            y = -s * x * x + 2 * s * x
            mPathMeasure.getSegment(y, y + 1, dst, true)
            canvas.drawPath(dst, mPaint)
        }


        super.onDraw(canvas)
    }

    fun dip2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale + 0.5f
    }
}