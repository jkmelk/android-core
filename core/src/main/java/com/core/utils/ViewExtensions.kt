package com.ucom.utils.extensions

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.CheckResult
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import kotlin.math.abs


const val DRAWER_OPENED = 1
const val DRAWER_CLOSED = 2

fun <T : View> T.animateColor(colorFrom: Int, colorTo: Int, body: T.(Int) -> Unit) {
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 300
    colorAnimation.addUpdateListener {
        val value = it.animatedValue as Int
        body(value)
    }
    colorAnimation.start()
}

fun View.spToPx(src: Int) = context.spToPx(src)

fun View.dpToPx(src: Int) = context.dpToPx(src)

fun View.dpToPx(src: Float) = context.dpToPx(src)

fun View.onClick(function: () -> Unit): View {
    var previousClickTimestamp: Long = 0

    setOnClickListener {
        val currentTimeStamp = System.currentTimeMillis()
        val lastTimeStamp = previousClickTimestamp
        previousClickTimestamp = currentTimeStamp
        if (abs(currentTimeStamp - lastTimeStamp) > 300) {
            function()
        }
    }
    return this
}

infix fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, this, false)

fun View.onApplyWindowInsets(body: WindowInsetsCompat.() -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        insets.body()
        ViewCompat.setOnApplyWindowInsetsListener(this, null)
        WindowInsetsCompat.CONSUMED
    }
}

fun ViewPager2.doOnPageChange(body: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            body(position)
        }
    })
}

fun isScreenReady(vararg editText: EditText, maxLength: Int = 0): Boolean {
    editText.forEach {
        if (it.text.toString().isEmpty()) {
            return false
        }
    }
    return true
}

fun needShowError(editText: EditText): Boolean {
    if (editText.text.toString().isEmpty()) {
        return true
    }
    return false
}

fun Button.validate(vararg editText: EditText, maxLength: Int = 0): Boolean {
    editText.forEach {
        if (it.text.toString().isEmpty()) {
            isEnabled = false
            return false
        }
    }
    isEnabled = true
    return true
}

fun Fragment.initKeyBoardRegListener(scroll: ScrollView, view: EditText) = KeyboardVisibilityEvent.registerEventListener(this.requireActivity()) { isOpen ->
    if (isOpen && view.isFocused) {
        scroll.smoothScrollTo(0, view.top)
    }
}

fun EditText.showKeyBoard() {
    requestFocus()
    val imm: InputMethodManager? = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.showToggleSoftKeyBoard() {
    requestFocus()
    val imm: InputMethodManager? = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun EditText.hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyBoard() {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.hideToggleSoftKeyBoard() {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
}

fun EditText.onCompleted(maxLength: Int = 6, body: (String) -> Unit) {
    addTextChangedListener {
        if (it.toString().length == maxLength)
            it?.toString()?.let { it1 -> body(it1) }
    }
}

fun DrawerLayout.onStateChanged(call: (Int) -> Unit) {
    addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerOpened(drawerView: View) {
            call(DRAWER_OPENED)
        }

        override fun onDrawerClosed(drawerView: View) {
            call(DRAWER_CLOSED)
        }

        override fun onDrawerStateChanged(newState: Int) {
        }
    })
}

fun TextView.toBlurText() {
    val balance = "-OOOO-"
    val endIndex = balance.length
    val string = SpannableString(balance)
    val blurMask: MaskFilter = BlurMaskFilter(32f, BlurMaskFilter.Blur.NORMAL)
    string.setSpan(MaskFilterSpan(blurMask), 0, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = string
}

fun TextView.toStyleUnderLine(color: Int, startPoint: Int, endPoint: Int, call: () -> Unit = {}) {
    val spannableString = SpannableString(text)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            call()
        }
    }
//    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    spannableString.setSpan(ForegroundColorSpan(color), startPoint, endPoint, 0)
    text = spannableString
}

fun TextView.toStyleUnderLines(textValue: String, color: Int, startPoint: Int, endPoint: Int) {
    val spannableString = SpannableString(textValue)
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    spannableString.setSpan(UnderlineSpan(), startPoint, endPoint, 0)
    spannableString.setSpan(ForegroundColorSpan(resources.getColor(color)), startPoint, endPoint, 0)
    text = spannableString
}

fun View.slideView(startHeight: Int = layoutParams.height, newHeight: Int = LinearLayout.LayoutParams.WRAP_CONTENT, duration: Long = 5000) {

    val slideAnimator = ValueAnimator
            .ofInt(startHeight, newHeight)
            .setDuration(duration);

    /* We use an update listener which listens to each tick
     * and manually updates the height of the view  */

    slideAnimator.addUpdateListener {
        val value = it.animatedValue
        layoutParams.height = value as Int
        requestLayout();
    }

    /*  We use an animationSet to play the animation  */

    val animationSet = AnimatorSet();
    animationSet.interpolator = AccelerateDecelerateInterpolator()
    animationSet.play(slideAnimator);
    animationSet.start()
}

fun ViewGroup.animateLayoutChange(duration: Long = 250) {
    val animateTransition = LayoutTransition()
    animateTransition.setDuration(300)
    animateTransition.enableTransitionType(LayoutTransition.CHANGING)
    layoutTransition = layoutTransition
}

fun TabLayout.tabSelected(call: (Int) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            call(selectedTabPosition)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    })
}

fun ViewPager2.selected(call: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            call(position)
        }
    })
}

fun EditText.onChanged(delay: Long = 0, call: (String) -> Unit) {
    addTextChangedListener {
        delayed(delay) { call(it.toString()) }
    }
}

@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun TextView.toBold(textValue: String, startPoint: Int, endPoint: Int) {
    val spannableString = SpannableString(textValue)
    spannableString.bold(startPoint, endPoint)
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    text = spannableString
}

fun SpannableString.bold(start: Int, end: Int): SpannableString {
    this.setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
    return this
}

fun SpannableString.color(color: String, start: Int, end: Int): SpannableString {
    this.setSpan(ForegroundColorSpan(Color.parseColor(color)), start, end, 0)
    return this
}

fun String.formatSixteenString(digits: String): String {
    val sb = StringBuilder()
    run {
        for (i in 0..15) {
            if (i != 0 && i % 4 == 0) {
                sb.append(' ') // insert every 4th char, except at end
            }
            sb.append(digits[i])
        }
    }
    return sb.toString()
}

fun View.getCollapseAnimation(): Animation {
    val initialHeight = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
//                visibility = View.GONE
            } else {
//                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                layoutParams.height = 700
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        override fun cancel() {
            super.cancel()
//            visibility = View.GONE
        }
    }

    animation.duration = (initialHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(animation)
    return animation
}

fun View.getExpandAnimation(): Animation {
    measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
    )
    val targetHeight = measuredHeight

    layoutParams.height = 1
    visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
                interpolatedTime: Float,
                transformation: Transformation?
        ) {
            layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        override fun cancel() {
            super.cancel()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    animation.duration = 200
    return animation
}

fun BottomSheetBehavior<*>.stateChanged(call: (Int) -> Unit) {
    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            call(newState)
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    })

}

fun EditText.actionDoneClicked(call: (String) -> Unit) {
    setOnEditorActionListener { textView, i, keyEvent ->
        if (i == EditorInfo.IME_ACTION_DONE) {
            call(text.toString())

        }
        return@setOnEditorActionListener false
    }
}
