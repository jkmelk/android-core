package com.core.presentation.dialog

import androidx.viewbinding.ViewBinding
import com.core.presentation.BaseViewModel
import com.core.presentation.FragmentResultCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBottomSheetVm<V : BaseViewModel, B : ViewBinding> : BaseBottomSheet<B>(), FragmentResultCallback {

    val viewModel: V by viewModel(createViewModel())

    override fun createBinding(): Class<B> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<B>

    open fun createViewModel(): KClass<V> {
        val instance =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<V>
        return instance.kotlin
    }

    override fun onStop() {
        viewModel.clearCall()
        super.onStop()
    }
}