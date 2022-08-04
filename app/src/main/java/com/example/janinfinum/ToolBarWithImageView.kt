package com.example.janinfinum

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.example.janinfinum.databinding.CustomToolbarBinding

class ToolBarWithImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: CustomToolbarBinding

    init {
        binding = CustomToolbarBinding.inflate(
            LayoutInflater.from(context), this
        )


        clipToPadding = false
        clipChildren = false

        setPadding(
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x)
        )


    }

    fun getImage(): ImageView {
        return binding.toolbarImage
    }

    fun setBitmapImage(bitmapImage: Bitmap) {
        binding.toolbarImage.setImageBitmap(bitmapImage)
    }

    fun getText(): TextView {
        return binding.toolbarText
    }

    fun setText(text: String) {
        binding.toolbarText.text = text
    }

}