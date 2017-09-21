package cn.wangzq.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * @author wzq
 * @email wangzhiqiang@17money.com
 * @date : 2017-09-11 09:25
 */
class SolarSystemView : View {
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

    fun init() {

        mPaint.color = Color.GRAY
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 1.0f
        mPaint.textSize = 20f
        mPaint.isAntiAlias = true

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

    }


    val mPaint = Paint()
    val mPath = Path()
    val mPathMeasure = PathMeasure()
    val pointDst = Path()


//    水星：0.24年（88日）
//    金星：0.62年（225日）
//    地球：1年（365日）
//    火星：1.88年（687日）
//    木星：11.86年（4333日）
//    土星：29.46年（10760日）
//    天王星：84年（30799日）
//    海王星：165年（60192日)


    var posArray = floatArrayOf(88f, 225f, 365f, 687f, 4333f, 10760f, 30799f, 60192f)
    var strArray = arrayOf("水星", "金星", "地球", "火星", "木星", "土星", "天王星", "海王星")

    var startTime = System.currentTimeMillis()

    override fun onDraw(canvas: Canvas) {


        var padding = if (width < height) paddingLeft + paddingRight else paddingBottom + paddingTop

        canvas.translate(width / 2f, height / 2f)

        var radius = if (width > height) height else width


        for (i in 1..8) {

            mPath.rewind()

            //绘制行星轨道
            mPaint.color = Color.GRAY
            mPaint.strokeWidth = 1.0f
            mPaint.style = Paint.Style.STROKE

            var realradius = radius / 2f * (i / 8f) - (padding)
            canvas.drawCircle(0f, 0f, realradius, mPaint)


            //绘制行星
            mPath.addCircle(0f, 0f, realradius, Path.Direction.CW)
            mPathMeasure.setPath(mPath, true)

            pointDst.rewind()

            mPaint.color = Color.GREEN
            mPaint.strokeWidth = 20f
            //行星公转周长

            var perimeter = mPathMeasure.length

            var difftime = System.currentTimeMillis() - startTime

            //(转动速度)( 周长/周期 )* 时间 = 转动长度
            var len = perimeter / posArray[i - 1] * difftime / 16f + posArray[i - 1]

            len %= perimeter
            //旋转方向
            len = perimeter - len
//            mPathMeasure.getSegment(len - 1, len, pointDst, true)
//            canvas.drawPath(pointDst, mPaint)

            val point = FloatArray(2)
            mPathMeasure.getPosTan(len, point, null)
            canvas.drawPoint(point[0], point[1], mPaint)
//            mPaint.strokeWidth=1f
//            mPaint.style=Paint.Style.FILL
//            mPaint.textSize=25f
//            mPathMeasure.getSegment(len-(mPaint.textSize*strArray[i-1].length) , len , pointDst, true)
//            canvas.drawTextOnPath(strArray[i-1],pointDst,0f,0f,mPaint)


        }


//        postInvalidate()
        postInvalidateDelayed(16)

        super.onDraw(canvas)
    }
}