package cn.wangzq.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * @author wzq
 * @email wangzhiqiang@17money.com
 * @date : 2017-05-27 12:35
 */

class MagicCircleView : View {
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var spec = Math.max(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(spec, spec)
    }


    private var mPaint: Paint = Paint()
    private var mPath: Path = Path()
    private val mValueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 360f)
    private val mPathMeasure: PathMeasure = PathMeasure()

    fun init() {
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 1F
        mPaint.isAntiAlias = true
//        mPaint.setShadowLayer(10f,5f,5f,Color.GREEN)

        mValueAnimator.duration = 3000L
        mValueAnimator.repeatCount = -1
//        mValueAnimator.repeatMode = ValueAnimator.REVERSE
        mValueAnimator.addUpdateListener {
            invalidate()
        }
        mValueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {


        var centerX = width / 2f
        var centerY = height / 2f
        var diameter = (width - paddingLeft - paddingRight) / 2f

        //移动坐标原点到中心
        canvas.translate(centerX, centerY)
        //旋转画布
        canvas.rotate(-90f)

        //画最大的圆
        canvas.drawCircle(0f, 0f, diameter, mPaint)
        //画第二个圆
        canvas.drawCircle(0f, 0f, diameter - 15, mPaint)

        //最大的六芒星
        drawStar(canvas, diameter - 15 - 10, 0f,true)
        //画六芒星中间的小星星 动的哦

        var t = mValueAnimator.animatedValue as Float
        drawStar(canvas, (diameter - 15 - 10) / 2 - 5, t,false)
        drawStar(canvas, ((diameter - 15 - 10) / 2 - 5) / 2 - 2, t + 90f,false)

        super.onDraw(canvas)
    }

    fun drawStar(canvas: Canvas, r: Float, rotate: Float ,showCircle:Boolean) {


        mPath.rewind()

        mPath.addCircle(0f, 0f, r, Path.Direction.CW)
        mPathMeasure.setPath(mPath, false)

        var size = mPathMeasure.length / 6

        var path1 = Path()
        var path2 = Path()

        for (i in 0..6 - 1) {

            var pos: FloatArray = FloatArray(2)
            var tan: FloatArray = FloatArray(2)
            mPathMeasure.getPosTan(size * i, pos, tan)
            if (i % 2 == 0) {
                if (i == 0) {
                    path1.moveTo(pos[0], pos[1])
                } else {
                    path1.lineTo(pos[0], pos[1])

                }
            } else {
                if (i == 1) {
                    path2.moveTo(pos[0], pos[1])
                } else {
                    path2.lineTo(pos[0], pos[1])
                }
            }


        }

        path1.close()
        path2.close()

        canvas.rotate(rotate)

        canvas.drawPath(path1, mPaint)
        canvas.drawPath(path2, mPaint)

        if(showCircle) {
            mPath.rewind()
            mPath.addCircle(0f, 0f, r/3*2, Path.Direction.CW)
            mPathMeasure.setPath(mPath, false)
            size = mPathMeasure.length / 6
            for (i in 0..6 - 1) {
                var pos: FloatArray = FloatArray(2)
                var tan: FloatArray = FloatArray(2)
                mPathMeasure.getPosTan(size * i, pos, tan)
                canvas.drawCircle(pos[0],pos[1],r/8,mPaint)
            }
        }

    }


}