package com.base.applicaton.screen.test_fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.base.applicaton.R
import com.base.applicaton.databinding.FragmentBlankBinding
import com.core.navigation.presentBottomSheet
import com.core.navigation.showFragment
import com.core.presentation.BaseVmFragment
import com.core.utils.subscribe
import com.data.model.response.ConfigResponse
import com.yt.utils.extensions.dpToPx
import com.yt.utils.extensions.onClick

class BlankFragment : BaseVmFragment<TestViewModel, FragmentBlankBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getConfigs()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.root.onClick { presentDialog() }
    }

    private fun presentDialog() {
        presentBottomSheet<FullScreenBottomSheetFragment>(requestKey = arrayOf("asdasd"))
    }

    private fun initObservers() = viewModel.run {
        configForUi.subscribe(viewLifecycleOwner) { handleConfigResult(it) }
    }

    private fun handleConfigResult(it: ConfigResponse) = binding.run {

    }

    override fun onFragmentResult(key: String, result: Bundle) = when (key) {
        "asddasd" -> {}
        "qwerer" -> {}
        else -> {}
    }
}