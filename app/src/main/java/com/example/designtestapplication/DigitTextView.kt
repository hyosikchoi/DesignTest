package com.example.designtestapplication

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import java.util.Locale

class DigitTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        init(context)
    }

    companion object {
        private const val ANIMATION_DURATION = 100
    }

    var currentTextView: TextView? = null
    var nextTextView: TextView? = null

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.digit_text_view, this)
        currentTextView = rootView.findViewById(R.id.currentTextView)
        nextTextView = rootView.findViewById(R.id.nextTextView)
        nextTextView?.translationY = height.toFloat()
        setValue(0)
    }

    fun setValue(desiredValue: Int) {
        if (currentTextView?.text.isNullOrEmpty()) {
            currentTextView?.text =
                String.format(Locale.getDefault(), "%d", desiredValue)
        }
        val oldValue = currentTextView?.text.toString().toInt()
        if (oldValue > desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue - 1)
            currentTextView?.animate()?.translationY(-height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.translationY = nextTextView?.height?.toFloat() ?: 0f
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue - 1)
                        currentTextView?.translationY = 0f
                        if (oldValue - 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })?.start()
        } else if (oldValue < desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue + 1)
            currentTextView?.animate()?.translationY(height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.translationY = -1 * (nextTextView?.height?.toFloat() ?: 0f)
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue + 1)
                        currentTextView?.translationY = 0f
                        if (oldValue + 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })?.start()
        }
    }
}
