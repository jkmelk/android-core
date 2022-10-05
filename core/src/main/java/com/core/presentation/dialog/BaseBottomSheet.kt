package com.core.presentation.dialog

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.core.loading_view.LoadingView
import com.core.logger.log
import com.core.prefrences.AppPreferences
import com.core.presentation.FragmentResultCallback
import com.core.presentation.ViewState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yt.core.R
import com.yt.utils.extensions.dpToPx
import com.yt.utils.extensions.hideKeyBoard
import org.koin.android.ext.android.inject
import java.lang.reflect.ParameterizedType

abstract class BaseBottomSheet<B : ViewBinding> : BottomSheetDialogFragment(), FragmentResultCallback {

    protected var hasResizeMode = false
    protected lateinit var binding: B
    protected val preferences by inject<AppPreferences>()
    private var topPadding = 0
    private var behavior: BottomSheetBehavior<View>? = null

    open fun createBinding(): Class<B> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.HelixBottomSheetStyle)
        val method = createBinding().getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, inflater) as B
        binding.root.isClickable = true
        binding.root.isFocusable = true
        dialog?.setOnShowListener { dialog -> setupFullHeight(dialog as BottomSheetDialog) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("Page") { this::class.java.name }
        if (hasResizeMode) initResizeMode()
        (dialog as BottomSheetDialog?)?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressDispatcher)
    }

    open fun handleLoadingEvent(it: ViewState, loadingView: LoadingView) = when (it) {
        ViewState.Loading -> loadingView.showLoading()
        else -> loadingView.hideLoading()
    }

    fun popBackStack() {
        activity?.hideKeyBoard()
        behavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun initResizeMode() {
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight)
                windowInsets.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemGestures())
            }
            windowInsets
        }
    }

    private val backPressDispatcher = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            dispatchBackPressed()
        }
    }

    protected fun isEnableBackDispatcher(isEnable: Boolean) {
        backPressDispatcher.isEnabled = isEnable
    }

    protected open fun dispatchBackPressed() {
        popBackStack()
    }

    override fun onFragmentResult(key: String, result: Bundle) {

    }

    /*Make dialog height full screen*/
    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val layoutParams = bottomSheet?.layoutParams
        if (layoutParams != null) {
            behavior = BottomSheetBehavior.from(bottomSheet)
            layoutParams.height = getWindowHeight()
            bottomSheet.layoutParams = layoutParams
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            behavior?.skipCollapsed = true
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /*Will remove Dim Background behind dialog*/
    protected fun removeDimBackGround() {
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    /*Calculate display height*/
    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels - dpToPx(topPadding)
    }

    /*Configure padding top*/
    fun setTopPadding(padding: Int) {
        this.topPadding = padding
    }
}