package com.base.applicaton.screen.test_fragment

import android.os.Bundle
import android.view.View
import com.base.applicaton.databinding.FragmentBlank2Binding
import com.core.presentation.BaseFragment
import com.yt.core.R
import com.yt.utils.extensions.delayed
import com.yt.utils.extensions.onClick


class BlankFragment2 : BaseFragment<FragmentBlank2Binding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.onClick {
//            activity?.supportFragmentManager?.restoreBackStack(BlankFragment::class.java.simpleName)
            activity?.supportFragmentManager?.beginTransaction()?.hide(this)?.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)?.commit()
            delayed(3000) {
                activity?
                .supportFragmentManager?
                .beginTransaction()?
                .show(this)?
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)?
                .commit()
            }
        }
    }
}