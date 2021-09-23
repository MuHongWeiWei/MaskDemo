# Android 新手引導頁面 遮罩View

##### 將自己想要的新手引導介面利用Mask的形式蓋到View的上方，可以建立自定義檢視的Mask利用OnDraw來執行遮罩。使用背景半透明，遮罩引導層是很常用的一個功能，所以我自己做一個範例來實現。

---

#### 文章目錄
<ol>
    <li><a href="#a">創建遮罩View</a></li>
    <li><a href="#b">添加遮罩View</a></li>
    <li><a href="#c">畫面布局</a></li>
    <li><a href="#d">View圖層</a></li>
    <li><a href="#e">Github</a></li>
</ol>

---

<a id="a"></a>
#### 1.創建遮罩View
```Kotlin
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
```

<a id="b"></a>
#### 2.添加遮罩View
```Kotlin
package com.example.maskdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    lateinit var decorView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

		//隱藏actionbar 不然高度會不同
        supportActionBar?.hide()
		//最底層View
        decorView = window.decorView.findViewById(android.R.id.content)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        //取得View
        val next = findViewById<AppCompatImageView>(R.id.next)
        val previous = findViewById<AppCompatImageView>(R.id.previous)
        val confirm = findViewById<AppCompatButton>(R.id.confirm)

        //設置xyr
        val maskView = MaskView(this, decorView)
        maskView.setButton(confirm, confirm.width / 1.5f)
        maskView.setNext(next, next.width / 1.5f)
        maskView.setPrevious(previous, previous.width / 1.5f)

        decorView.addView(maskView)
    }
}
```

<a id="c"></a>
#### 3.畫面布局
```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.appcompat.widget.AppCompatImageView
        android:id="@+id/next"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_marginEnd="50dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="@+id/previous"
        app:srcCompat="@drawable/next" />

    <androidx.appcompat.widget.AppCompatImageView
        android:id="@+id/previous"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_marginStart="50dp"
        android:layout_marginTop="100dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/previous" />

    <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/confirm"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="確認"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="@+id/next"
        app:layout_constraintStart_toStartOf="@+id/previous"
        app:layout_constraintTop_toBottomOf="@+id/previous" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

<a id="d"></a>
#### 4.View圖層
[![](https://badgameshow.com/fly/wp-content/uploads/2020/11/1605850984299-300x198.jpg)](https://badgameshow.com/fly/wp-content/uploads/2020/11/1605850984299.jpg)

[![](https://badgameshow.com/fly/wp-content/uploads/2020/11/1605851579945-300x179.jpg)](https://badgameshow.com/fly/wp-content/uploads/2020/11/1605851579945.jpg)

<a id="e"></a>
#### 5.Github
<a href="https://github.com/MuHongWeiWei/MaskDemo">Android 新手引導頁面 遮罩View</a>
