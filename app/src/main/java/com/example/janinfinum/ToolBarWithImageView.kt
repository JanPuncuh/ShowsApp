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

    var binding: CustomToolbarBinding = CustomToolbarBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {

        clipToPadding = false
        clipChildren = false

        setPadding(
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_2x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x)
        )

        context.withStyledAttributes(attrs, R.styleable.ToolBarWithImageView) {
            //sets "imageSrc" custom attribute
            binding.toolbarImage
                .setImageResource(
                    this.getResourceId(
                        R.styleable.ToolBarWithImageView_imageSrc, -1
                    )
                )

            //text attribute
            binding.toolbarText.text = getString(R.styleable.ToolBarWithImageView_text)

        }

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