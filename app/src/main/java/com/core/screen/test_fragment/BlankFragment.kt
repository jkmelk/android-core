package com.core.screen.test_fragment

import android.os.Bundle
import android.view.View
import com.core.application.databinding.FragmentBlankBinding
import com.core.logger.log
import com.core.navigation.presentBottomSheet
import com.core.presentation.BaseVmFragment
import com.core.utils.onClick
import com.core.utils.subscribe
import com.data.model.response.ConfigResponse

class BlankFragment : BaseVmFragment<TestViewModel, FragmentBlankBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        "asddasd" -> log { "asddasd" }
        else -> {}
    }
}