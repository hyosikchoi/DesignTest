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

        private const val INTERVAL = 1
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
        // 현재 값이 새로 입력된 값보다 크면
        if (oldValue > desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue - INTERVAL)
            // translationY > 0 면 아래로, translationY < 0 면 위로
            // 현재 값을 위로 올리는 애니메이션을 취하고
            currentTextView?.animate()?.translationY(-height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()

            nextTextView?.translationY = nextTextView?.height?.toFloat() ?: 0f
            // 현재 값-interval 한 text를 제자리로 올리는 애니메이션을 취한다.
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        // 애니메이션 종료 시 현재 값에 - interval 하고 nextTextView 와 다시 겹쳐 놓는다.
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue - INTERVAL)
                        currentTextView?.translationY = 0f
                        // 구간 만큼 차감 시 아직 입력값이 아니라면 재귀를 돌린다.
                        if (oldValue - INTERVAL != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })?.start()
        } else if (oldValue < desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue + INTERVAL)
            currentTextView?.animate()?.translationY(height.toFloat())?.setDuration(
                ANIMATION_DURATION.toLong()
            )?.start()
            nextTextView?.translationY = -1 * (nextTextView?.height?.toFloat() ?: 0f)
            nextTextView?.animate()?.translationY(0f)?.setDuration(ANIMATION_DURATION.toLong())
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentTextView?.text =
                            String.format(Locale.getDefault(), "%d", oldValue + INTERVAL)
                        currentTextView?.translationY = 0f
                        if (oldValue + INTERVAL != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })?.start()
        }
    }
}
