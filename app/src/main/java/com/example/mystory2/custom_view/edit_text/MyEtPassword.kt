package com.example.mystory2.custom_view.edit_text

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.mystory2.R



class MyEtPassword: AppCompatEditText {
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

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(s.toString().isNotEmpty()){
                    error = if(passwordValidation(s.toString())) null else context.getString(
                        R.string.invalid_password)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "*********"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    private fun passwordValidation(password:String):Boolean{
        return password.length >=6
    }
}