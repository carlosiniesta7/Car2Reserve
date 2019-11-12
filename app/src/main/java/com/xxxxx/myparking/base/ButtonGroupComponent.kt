package com.xxxxx.myparking.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.xxxxx.myparking.R
import kotlinx.android.synthetic.main.button_group_component.view.*

class ButtonGroupComponent @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle:Int = 0
    ):ConstraintLayout(context,attributeSet,defStyle){

    init {
        LayoutInflater.from(context).inflate(R.layout.button_group_component,this,true)
        attributeSet?.let{
            val typedArray = context.obtainStyledAttributes(it,R.styleable.ButtonGroupComponent,0,0)
            val color = ContextCompat.getColor(context,typedArray.getResourceId(R.styleable.ButtonGroupComponent_buttonBackgroundColor,R.color.colorAccent))
            leftButton.setBackgroundColor(color)
            rightButton.setBackgroundColor(color)
        }
    }

    fun leftButtonSetOnClickListener (clickListener: OnClickListener){
        leftButton.setOnClickListener(clickListener)
    }

    fun rightButtonSetOnClickListener (clickListener: OnClickListener){
        rightButton.setOnClickListener(clickListener)
    }

    fun leftButtonSetVisibility(visibility:Int){
        leftButton.visibility = visibility
    }

    fun rightButtonSetVisibility(visibility:Int){
        rightButton.visibility = visibility
    }

    fun leftButtonSetText(input:String){
        leftButton.text = input
    }

}