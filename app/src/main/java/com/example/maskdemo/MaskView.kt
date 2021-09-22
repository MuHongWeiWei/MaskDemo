package com.example.maskdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * Author: Wade
 * E-mail: tony91097@gmail.com
 * Date: 2021/9/22
 */
@SuppressLint("ViewConstructor")
class MaskView(context: Context, private val decorView: FrameLayout) : View(context) {

    private val path = Path()
    private val maskPaint = Paint().apply {
        color = Color.parseColor("#90000000")
    }

    private var nextX = 0f
    private var nextY = 0f
    private var nextR = 0f

    private var previousX = 0f
    private var previousY = 0f
    private var previousR = 0f

    private var confirmX = 0f
    private var confirmY = 0f
    private var confirmR = 0f

    //設置View遮罩位子
    fun setNext(view: View, radius: Float) {
        this.nextX = view.x + view.width / 2
        this.nextY = view.y + view.height / 2
        this.nextR = radius
    }

    //設置View遮罩位子
    fun setPrevious(view: View, radius: Float) {
        this.previousX = view.x + view.width / 2
        this.previousY = view.y + view.height / 2
        this.previousR = radius
    }

    //設置View遮罩位子
    fun setButton(view: View, radius: Float) {
        this.confirmX = view.x + view.width / 2
        this.confirmY = view.y + view.height / 2
        this.confirmR = radius
    }

    //CW順時針畫 CCW逆時針畫 挖洞
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
            path.addCircle(confirmX, confirmY, confirmR, Path.Direction.CCW)
            path.addCircle(previousX, previousY, previousR, Path.Direction.CCW)
            path.addCircle(nextX, nextY, nextR, Path.Direction.CCW)
            drawPath(path, maskPaint)
        }
    }

    //觸碰到指定座標移除遮罩
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            if (MotionEvent.ACTION_DOWN == action) {
                if (confirmX - confirmR < event.x && confirmX + confirmR > event.x && confirmY - confirmR < event.y && confirmY + confirmR > event.y) {
                    decorView.removeView(this@MaskView)
                }
            }
        }
        return true
    }
}
