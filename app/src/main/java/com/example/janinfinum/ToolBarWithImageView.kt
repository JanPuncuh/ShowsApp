package com.example.janinfinum

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.janinfinum.databinding.CustomToolbarBinding

class ToolBarWithImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: CustomToolbarBinding

    init {
        binding = CustomToolbarBinding.inflate(LayoutInflater.from(context), this)


        clipToPadding = false
        clipChildren = false

        setPadding(
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x)
        )
    }
}