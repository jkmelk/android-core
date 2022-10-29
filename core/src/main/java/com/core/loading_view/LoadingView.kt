package com.core.loading_view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.core.R
import com.core.databinding.LoadingIndicatorViewBinding
import com.core.utils.delayed
import com.core.utils.dpToPx

class LoadingView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private val binding: LoadingIndicatorViewBinding

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.loading_indicator_view, this, true)
        binding = LoadingIndicatorViewBinding.bind(root)
        isClickable = true
        isFocusable = true
        elevation = dpToPx(10).toFloat()
        translationZ = dpToPx(10).toFloat()
        isVisible = false
    }

    fun changeLoadingStatus(isVisible: Boolean) {
        if (isVisible) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    fun showLoading(dimmed: Boolean = false) = binding.run {
        setBackgroundColor(Color.TRANSPARENT)
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        indicatorView.startAnimation(animation)
        isVisible = true
    }

    fun hideLoading() = binding.run {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        indicatorView.startAnimation(animation)
        delayed(900) {
            isVisible = false
        }
    }
}

