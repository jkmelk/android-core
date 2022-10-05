package com.base.applicaton.screen.test_fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.base.applicaton.databinding.FragmentBlank2Binding
import com.core.presentation.dialog.BaseBottomSheet
import com.yt.utils.extensions.delayed

class FullScreenBottomSheetFragment : BaseBottomSheet<FragmentBlank2Binding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeDimBackGround()
        setTopPadding(40)
        delayed(300) {
            setFragmentResult("asdasd", bundleOf())
        }
    }
}