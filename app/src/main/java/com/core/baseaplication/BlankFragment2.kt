package com.core.baseaplication

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.core.baseaplication.databinding.FragmentBlank2Binding
import com.core.presentatin.BaseViewModel
import com.core.presentatin.BaseVmFragment
import com.core.utils.extensions.delayed

const val Some_KEY = "some_key"

class BlankFragment2 : BaseVmFragment< BaseViewModel,FragmentBlank2Binding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delayed(5000) {
            setFragmentResult(Some_KEY, bundleOf())
        }
    }
}