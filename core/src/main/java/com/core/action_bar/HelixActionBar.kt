package com.core.action_bar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.core.R
import com.core.databinding.CommonStatusBarBinding
import com.core.utils.onClick

class HelixActionBar(
        context: Context,
        attributeSet: AttributeSet? = null,
) : ConstraintLayout(context, attributeSet) {


    private val binding: CommonStatusBarBinding

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.common_status_bar, this, true)
        binding = CommonStatusBarBinding.bind(root)

        val styledAttrs = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar)
        val title = styledAttrs.getString(R.styleable.ActionBar_title) ?: ""
        val hasBack = styledAttrs.getBoolean(R.styleable.ActionBar_hasBack, true)
        val hasActionButton = styledAttrs.getBoolean(R.styleable.ActionBar_hasActionButton, false)
        val bacButton = styledAttrs.getDrawable(R.styleable.ActionBar_bacButton)
        val actionButton = styledAttrs.getDrawable(R.styleable.ActionBar_actionButton)
        styledAttrs.recycle()

        makeView(title, hasBack, hasActionButton, bacButton, actionButton)
        isClickable = true
        isFocusable = true
    }

    private fun makeView(title: String, hasBack: Boolean,
                         hasActionButton: Boolean,
                         bacButton: Drawable?,
                         actionButton: Drawable?) {
        binding.commonTitle.text = title
        val visibility = if (hasBack) VISIBLE else INVISIBLE
        binding.commonBackButton.visibility = visibility
        binding.commonNextButton.isVisible = hasActionButton
        binding.commonBackButton.setImageDrawable(bacButton)
        binding.commonNextButton.setImageDrawable(actionButton)
    }

    fun setBackClickListener(call: () -> Unit) {
        binding.commonBackButton.onClick { call() }
    }

    fun setActionButtonListener(call: () -> Unit) {
        binding.commonNextButton.onClick { call() }
    }

    fun setTitle(title: String) {
        binding.commonTitle.text = title
    }
}