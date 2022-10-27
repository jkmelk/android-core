package com.ucom.utils.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.provider.Settings

import org.koin.core.context.KoinContextHandler



internal fun runOnUiThread(action: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        action()
        return
    }

    val handler = Handler(Looper.getMainLooper())
    handler.post(action)
}

fun Context.spToPx(src: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, src.toFloat(), resources.displayMetrics)
            .toInt()
}

fun Context.dpToPx(src: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, src.toFloat(), resources.displayMetrics)
            .toInt()
}

fun Fragment.dpToPx(src: Int): Int {
    return requireContext().dpToPx(src)
}

fun Context.dpToPx(src: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, src, resources.displayMetrics)
}

fun Activity.string(@StringRes resId: Int): String {
    return getString(resId)
}

fun Fragment.string(@StringRes resId: Int): String {
    return requireActivity().getString(resId)
}

fun color(@ColorRes resId: Int): Int {
    val app = KoinContextHandler.get().get<Application>()
    return ContextCompat.getColor(app, resId)
}

fun delayed(milliseconds: Long, body: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(body, milliseconds)
}

fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.statusBarColor = Color.TRANSPARENT
        window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            window.navigationBarColor = getColor(R.color.transparent)
            it.hide(WindowInsets.Type.systemBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

fun Activity.initStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
//    window.navigationBarColor = ContextCompat.getColor(this, R.color.background_color)
}

fun deviceName(): String = Build.MODEL

fun Context.deviceToken(): String = Settings.Secure.getString(
        contentResolver, Settings.Secure.ANDROID_ID
)

fun Context.getDeviceDensity(): String {
    return resources.displayMetrics.density.toString()
}




