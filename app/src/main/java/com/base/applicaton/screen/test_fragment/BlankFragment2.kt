package com.base.applicaton.screen.test_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.base.applicaton.databinding.FragmentBlank2Binding
import com.core.logger.log
import com.core.navigation.presentFragment
import com.core.presentation.BaseFragment
import com.yt.core.R
import com.yt.utils.extensions.delayed
import com.yt.utils.extensions.onClick


class BlankFragment2 : BaseFragment<FragmentBlank2Binding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("qwerty") { key, _ ->
            log { "asdsd" }
        }
    }

    override fun onFragmentResult(key: String, result: Bundle) {
        log { key }
    }


}