package com.core.presentation

import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseVmFragment<V : BaseViewModel, B : ViewBinding> : BaseFragment<B>() {

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