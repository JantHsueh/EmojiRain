package me.xuexuan

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import me.xuexuan.raingroup.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author xuexuan https://github.com/JantHsueh
 * @date 2020年1月7日16:21:29
 */

class RainViewGroup : ConstraintLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }


    private var mPaint: Paint? = null
    //图片处理
    private var mMatrix: Matrix? = null
    private var mRandom: Random? = null
    //判断是否运行的，默认没有
    private var mIsRun: Boolean = false
    //循环次数，如果是-1 无限循环
    private var mTimes = 1
    private var mAlways = false
    //屏幕上最多显示多少个
    private var mAmount = 20
    //表情包集合
    private var mBitmapList: MutableList<ItemEmoje> = ArrayList<ItemEmoje>()

    private var mBitmapRecycleList: MutableList<ItemEmoje> = ArrayList<ItemEmoje>()
    //表情图片
    private var mImgResId = R.drawable.star


    init {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isFilterBitmap = true
        mPaint!!.isDither = true
        mMatrix = Matrix()
        mRandom = Random()

    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mIsRun) {
            //用于判断表情下落结束，结束即不再进行重绘
            var isInScreen = false
            run {
                var i = 0
                while (i < mBitmapList.size) {
                    mMatrix!!.reset()
                    //缩放
                    mMatrix!!.setScale(mBitmapList[i].scale, mBitmapList[i].scale)
                    //下落过程坐标
                    mBitmapList[i].x = mBitmapList[i].x + mBitmapList[i].offsetX
                    mBitmapList[i].y = mBitmapList[i].y + mBitmapList[i].offsetY

                    //位移
                    mMatrix!!.postTranslate(
                        mBitmapList[i].x.toFloat(),
                        mBitmapList[i].y.toFloat()
                    )

                    canvas.drawBitmap(mBitmapList[i].bitmap, mMatrix!!, mPaint)
                    if (mBitmapList[i].y <= height && mBitmapList[i].x <= width) {//当表情仍在视图内，则继续重绘
                        isInScreen = true
                    } else {
                        recycle(mBitmapList[i])
                        i--
                    }
                    i++
                }
            }

            mIsRun = isInScreen
            if (mAlways && mBitmapList.size < ((mAmount* 4)/ 5)) {
                for (i in 0..(mAmount / 5)) {
                    addItemToBitmapList()
                }
                mIsRun = true
            }

            postInvalidate()
        } else {
            release()
        }
    }

    /**
     * 释放资源
     */
    private fun release() {
        if (mBitmapList.size > 0) {
            for (itemEmoje in mBitmapList) {
                if (itemEmoje.bitmap?.isRecycled == false) {
                    itemEmoje.bitmap?.recycle()
                }
            }
            mBitmapList.clear()
        }

        if (mBitmapRecycleList.size > 0) {
            for (itemEmoje in mBitmapRecycleList) {
                if (itemEmoje.bitmap?.isRecycled == false) {
                    itemEmoje.bitmap?.recycle()
                }
            }
            mBitmapRecycleList.clear()
        }

    }


    /**
     * 回收资源
     */
    private fun recycle(item: ItemEmoje) {

        if (mBitmapList.size > 0) {
            mBitmapList.remove(item)
        }

        if (mBitmapRecycleList.size > 50) {

            if (mBitmapRecycleList[0].bitmap?.isRecycled == false) {
                mBitmapRecycleList[0].bitmap?.recycle()
            }
            mBitmapRecycleList.removeAt(0)
        }
        mBitmapRecycleList.add(item)

    }


    companion object {
        const val INFINITE = -1
    }

/**
 * 设置表情雨资源id
 */
    fun setImgResId(mImgResId: Int) {
        this.mImgResId = mImgResId
    }

/**
 * 设置表情的缩放比例
 */
    fun setScale(scaleRandom: Int, scaleOffset: Int) {
        mScaleRandom = scaleRandom
        mScaleOffset = scaleOffset
    }

/**
 * 设置x轴的偏移，每一次绘制表情在x轴上的偏移量
 */
    fun setX(xRandom: Int, xOffset: Int) {
        mXRandom = xRandom
        mXOffset = xOffset
    }

    /**
     * 设置y轴的偏移，每一次绘制表情在y轴上的偏移量
     */
    fun setY(yRandom: Int, yOffset: Int) {
        mYRandom = yRandom
        mYOffset = yOffset
    }


    /**
     * 设置表情落下时的，初始x值的范围，也就在指定屏幕宽度的范围内落下。
     * 避免表情的随机的x = 0（x = 屏幕宽度）时，xOffset为负值（正值），落下很小的距离，表情就划出屏幕的问题
     */
    fun setWidth(widthRandom: Int, widthOffset: Int) {
        mWidthRandom = widthRandom
        mWidthOffset = widthOffset
    }

    fun setTimes(times: Int = INFINITE) {
        if (times == INFINITE) {
            mAlways = true
            this.mTimes = 1
        } else {
            mAlways = false
            this.mTimes = times
        }
    }


    fun setAmount(num: Int = mAmount) {
        mAmount = num
    }


    fun start() {
        this.mIsRun = true
        initData(mTimes * mAmount)
        invalidate()
    }

    fun stop() {
        this.mIsRun = false
        this.mAlways = false
    }

    private fun initData(num: Int = mAmount) {
        release()
        for (i in 0 until num) {
            addItemToBitmapList()
        }
    }

    private fun addItemToBitmapList() {

        val itemEmoje: ItemEmoje
        if (mBitmapRecycleList.size > 0) {
            itemEmoje = createItem(mBitmapRecycleList[0])
            mBitmapRecycleList.removeAt(0)
        } else {
            itemEmoje = createItem()
        }
        mBitmapList.add(itemEmoje)
    }


    private var mWidthRandom = 200
    private var mWidthOffset = 100

    private var mXRandom = 6
    private var mXOffset = -3

    private var mYRandom = 5
    private var mYOffset = 10

    private var mScaleRandom = 30
    private var mScaleOffset = 70

    private fun createItem(item: ItemEmoje? = null): ItemEmoje {
        var itemEmoje = item

        if (itemEmoje == null) {
            itemEmoje = ItemEmoje()
            itemEmoje.bitmap = BitmapFactory.decodeResource(resources, mImgResId)
        }

        var width = width
        var height = height

        if (width == 0 || height == 0) {
            val dm = resources.displayMetrics
            width = dm.widthPixels
            height = dm.heightPixels
        }

        //起始横坐标在[100,getWidth()-100) 之间
        itemEmoje.x = mRandom!!.nextInt(width - mWidthRandom) + mWidthOffset
        //起始纵坐标在(-getHeight(),0] 之间，即一开始位于屏幕上方以外
        itemEmoje.y = -mRandom!!.nextInt(mTimes * height)

        //横向偏移[-3,3) ，即左右摇摆区间
        itemEmoje.offsetX = mRandom!!.nextInt(mXRandom) + mXOffset
        //纵向偏移(11,16] ，即下拉速度
        itemEmoje.offsetY = mRandom!!.nextInt(mYRandom) + mYOffset
        //缩放比例[0.7,1) 之间
        itemEmoje.scale = (mRandom!!.nextInt(mScaleRandom) + mScaleOffset).toFloat() / 100f

        return itemEmoje
    }
}
