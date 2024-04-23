package com.example.designtestapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val digitTextView1: DigitTextView by lazy { findViewById(R.id.digitTextView1) }
    private val digitTextView2: DigitTextView by lazy { findViewById(R.id.digitTextView2) }
    private val digitTextView3: DigitTextView by lazy { findViewById(R.id.digitTextView3) }
    private val digitTextView4: DigitTextView by lazy { findViewById(R.id.digitTextView4) }
    private val digitTextView5: DigitTextView by lazy { findViewById(R.id.digitTextView5) }
    private val digitTextView6: DigitTextView by lazy { findViewById(R.id.digitTextView6) }
    private val digitTextView7: DigitTextView by lazy { findViewById(R.id.digitTextView7) }

    private var timer: Timer = Timer()

    private var task: TimerTask? = null

    private var fuelValue: Double = 0.0

    private val lottieAnimationView: LottieAnimationView by lazy { findViewById(R.id.lottieAnimationView) }

    private var tankMin: Int = 0

    private var tankMax: Int = 10

    private var isEndReverse: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lottieAnimationView.setAnimation("fuel_box.json")
//        lottieAnimationView.repeatMode = LottieDrawable.REVERSE
        lottieAnimationView.addAnimatorUpdateListener {
            val progress = (it.animatedValue as Float * 100).toInt()
            if(progress == 0 && !isEndReverse){
                lottieAnimationView.speed = 1.0f
                isEndReverse = true
                lottieAnimationView.setMinAndMaxFrame(tankMin, tankMax)
            }
        }

        task = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    setDigitText()
                    if(tankMin == 100 && tankMax == 110 && isEndReverse) setReverseTank()
                    else {
                        if(isEndReverse) setTank()
                    }
                }
            }
        }

        timer.schedule(task, 1000, 1000)

    }


    override fun onPause() {
        super.onPause()
        lottieAnimationView.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        lottieAnimationView.playAnimation()
    }

    private fun setDigitText() {

        fuelValue += 0.000155
        val fuelValueString = BigDecimal.valueOf(fuelValue).setScale(6,RoundingMode.HALF_UP).toString()
        val fuelInteger = fuelValueString.split(".")[0]
        val fuelDecimalPoint = fuelValueString.split(".")[1]

        digitTextView1.setValue(fuelInteger.toInt())
        digitTextView2.setValue(fuelDecimalPoint[0].digitToInt())
        digitTextView3.setValue(fuelDecimalPoint[1].digitToInt())
        digitTextView4.setValue(fuelDecimalPoint[2].digitToInt())
        digitTextView5.setValue(fuelDecimalPoint[3].digitToInt())
        digitTextView6.setValue(fuelDecimalPoint[4].digitToInt())
        digitTextView7.setValue(fuelDecimalPoint[5].digitToInt())
    }

    private fun setTank() {
        tankMin += 10
        tankMax += 10
        lottieAnimationView.setMinAndMaxFrame(tankMin, tankMax)

    }

    private fun setReverseTank() {
        isEndReverse = false
        lottieAnimationView.speed = -1.0f
        lottieAnimationView.setMinAndMaxFrame(0, 100)
        tankMin = 0
        tankMax = 10
    }
}