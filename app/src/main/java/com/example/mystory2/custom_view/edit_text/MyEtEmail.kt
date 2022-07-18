package com.example.mystory2.custom_view.edit_text

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystory2.R


class MyEtEmail : AppCompatEditText{
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        clearButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    error=if (emailValidation(s.toString())) null else context.getString(R.string.invalid_email)
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "example:Email@gmail.com"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    private fun emailValidation(s:String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

}