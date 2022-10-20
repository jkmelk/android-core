package com.base.applicaton.screen.test_fragment

import android.os.Bundle
import android.view.View
import com.base.applicaton.databinding.FragmentBlank2Binding
import com.core.presentation.dialog.BaseBottomSheet

class FullScreenBottomSheetFragment : BaseBottomSheet<FragmentBlank2Binding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeDimBackGround()
        setTopPadding(20)
        setIsResizeEnabled(true)
    }
}