package com.sample.mobile.screen.sample


import android.os.Bundle
import android.view.View
import com.core.logger.log
import com.core.navigation.presentFragment
import com.core.presentation.BaseVmFragment
import com.core.utils.delayed
import com.core.utils.onClick
import com.core.utils.subscribe
import com.data.model.response.ConfigResponse
import com.sample.mobile.R
import com.sample.mobile.databinding.FragmentSampleBinding

class SampleFragment : BaseVmFragment<SampleViewModel, FragmentSampleBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initListeners() = binding.run {
        root.onClick { showDialog() }
    }

    private fun showDialog() {
        presentFragment<SampleFragment>()
    }

    private fun initObservers() = viewModel.run {
        configFlow.subscribe(viewLifecycleOwner) { handleConfigFlow(it) }
    }

    private fun handleConfigFlow(it: ConfigResponse) {
        log { it }
    }

}