package com.core.baseaplication

import android.os.Bundle
import android.view.View
import com.core.baseaplication.databinding.FragmentBlankBinding
import com.core.logger.log
import com.core.navigation.showFragment
import com.core.presentatin.BaseFragment
import com.core.utils.extensions.delayed

class BlankFragment : BaseFragment<FragmentBlankBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delayed(3000) {
            showFragment<BlankFragment2>(requestKey = arrayOf(Some_KEY))
        }
    }

    override fun onFragmentResult(key: String, result: Bundle) {
        super.onFragmentResult(key, result)
        log("asd") { key }
    }
}