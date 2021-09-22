package com.example.maskdemo

import android.content.res.Resources
import android.util.TypedValue

/**
 * Author: Wade
 * E-mail: tony91097@gmail.com
 * Date: 2021/9/22
 */


val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )