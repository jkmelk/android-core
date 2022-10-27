package com.core.view_binding

import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun < T : ViewBinding> Fragment.viewBinding(bindingClass: Class<T>) = FragmentViewBindingDelegate(bindingClass, this)

@Keep
class FragmentViewBindingDelegate<T : ViewBinding>(bindingClass: Class<T>, private val fragment: Fragment) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }
        val invoke = bindMethod.invoke(null, thisRef.requireView()) as T
        return invoke.also { this.binding = it }
    }
}