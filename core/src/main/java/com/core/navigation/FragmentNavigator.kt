package com.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.core.HelixApp
import com.core.presentation.BaseFragment
import com.core.presentation.FragmentResultCallback
import com.core.presentation.dialog.BaseBottomSheet
import com.core.R

inline fun <reified FRAGMENT : Fragment> Fragment.presentFragment(animate: Boolean = true,
                                                                  animationType: AnimationType = AnimationType.LEFT_TO_RIGHT,
                                                                  backStack: Boolean = true,
                                                                  container: Int = HelixApp.context.getConfig().mainContainer,
                                                                  openType: OpenType = OpenType.ADD,
                                                                  vararg arguments: Pair<String, Any?>,
                                                                  requestKey: Array<String> = arrayOf()) {
    activity?.let {
        initFragment<FRAGMENT>(it, it.supportFragmentManager, animate, animationType, backStack,
                container, openType, arguments = arguments)
        requestKey.forEach { reqKey ->
            it.supportFragmentManager.setFragmentResultListener(reqKey, this) { key, bundle ->
                if (this is FragmentResultCallback) {
                    this.onFragmentResult(key, bundle)
                } else {
                    throw InvalidFragmentTypeException("Result callback is available only in BaseFragment subclasses")
                }
            }
        }
    } ?: run {
        throw NoViewAttachedException("Activity is null ${this::class.java.name}")
    }
}

inline fun <reified FRAGMENT : BaseBottomSheet<*>> Fragment.presentBottomSheet(vararg arguments: Pair<String, Any?>,
                                                                               requestKey: Array<String> = arrayOf()) {
    activity?.let {
        val tag = FRAGMENT::class.java.name
        val fragmentManager = childFragmentManager
        var fragment = fragmentManager.findFragmentByTag(tag)
        fragment = fragmentManager.fragmentFactory.instantiate(context!!.classLoader, tag)
        fragment.arguments = bundleOf(*arguments)
        if (fragment is BaseBottomSheet<*>) {
            fragment.show(fragmentManager, tag)
        }
        requestKey.forEach { reqKey ->
            fragmentManager.setFragmentResultListener(reqKey, this) { key, bundle ->
                if (this is FragmentResultCallback) {
                    this.onFragmentResult(key, bundle)
                }
            }
        }
    } ?: run {
        throw NoViewAttachedException("Activity is null ${this::class.java.name}")
    }
}

inline fun <reified FRAGMENT : Fragment> AppCompatActivity.presentFragment(animate: Boolean = true,
                                                                           animationType: AnimationType = AnimationType.LEFT_TO_RIGHT,
                                                                           backStack: Boolean = true,
                                                                           container: Int = HelixApp.context.getConfig().mainContainer,
                                                                           openType: OpenType = OpenType.ADD,
                                                                           vararg arguments: Pair<String, Any?>) {
    initFragment<FRAGMENT>(this, supportFragmentManager, animate, animationType, backStack,
            container, openType, arguments = arguments)
}


inline fun <reified FRAGMENT : BaseFragment<*>> Fragment.fragment(): FRAGMENT? {
    return activity?.supportFragmentManager?.let {
        fragment<FRAGMENT>(it)
    } ?: kotlin.run {
        null
//        throw NoViewAttachedException("Activity is null ${this::class.java.name}")
    }
}

inline fun <reified FRAGMENT> initFragment(context: Context, manager: FragmentManager,
                                           animate: Boolean,
                                           animationType: AnimationType = AnimationType.LEFT_TO_RIGHT,
                                           backStack: Boolean,
                                           container: Int,
                                           openType: OpenType = OpenType.ADD,
                                           vararg arguments: Pair<String, Any?>) {
    val tag = FRAGMENT::class.java.name
    var fragment = manager.findFragmentByTag(tag)

    /*if (fragment != null) {
        val currentFragment = fragment ?: return
        manager.beginTransaction().remove(currentFragment).commit()
    }*/
    fragment = manager.fragmentFactory.instantiate(context.classLoader, tag)



    fragment.arguments = bundleOf(*arguments)
    inTransaction(manager, animate, animationType) {
        if (openType == OpenType.ADD) {
            add(container, fragment, tag)
        } else
            replace(container, fragment, tag)
        if (backStack)
            addToBackStack(tag)
    }

}

inline fun <reified FRAGMENT : BaseFragment<*>> fragment(manager: FragmentManager): FRAGMENT? {
    val tag = FRAGMENT::class.java.name
    return manager.findFragmentByTag(tag) as? FRAGMENT
}

inline fun inTransaction(fragmentManager: FragmentManager, animate: Boolean, animationType: AnimationType, transaction: FragmentTransaction.() -> Unit) {
    fragmentManager.commit {
        setReorderingAllowed(true)
        if (animate) {
            when (animationType) {
                AnimationType.LEFT_TO_RIGHT -> {
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right)
                }
                AnimationType.BOTTOM_TO_TOP -> {
                    setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up,
                            R.anim.slide_in_down, R.anim.slide_out_down)
                }
            }
        } else {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        transaction()
    }
}

inline fun FragmentManager.open(block: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        block()
        commit()
    }
}

inline fun <reified FRAGMENT : BaseFragment<*>> Fragment.popTo() = try {
    val fragmentsList = activity?.supportFragmentManager?.fragments
    fragmentsList?.let { fragments ->
        for (it in fragments.lastIndex downTo 0) {
            if (fragments[it] !is FRAGMENT) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                break
            }
        }
    }
} catch (e: IllegalStateException) {
    activity?.supportFragmentManager?.popBackStack()
}
