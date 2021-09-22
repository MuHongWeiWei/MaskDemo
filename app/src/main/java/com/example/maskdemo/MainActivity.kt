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

        supportActionBar?.hide()
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