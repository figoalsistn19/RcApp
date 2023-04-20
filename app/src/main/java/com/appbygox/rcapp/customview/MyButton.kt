package com.appbygox.rcapp.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.appbygox.rcapp.R

class MyButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @SuppressLint("SetTextI18n")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = enabledBackground
        setTextColor(txtColor)
        gravity = Gravity.CENTER
    }
    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.black_soft)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.btn_main) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.btn_bg) as Drawable
    }
}