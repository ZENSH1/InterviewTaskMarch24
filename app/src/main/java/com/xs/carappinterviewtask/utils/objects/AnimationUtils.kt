package com.xs.carappinterviewtask.utils.objects

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.xs.carappinterviewtask.data.enums.CustomAnimation
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.gone
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.invisible
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.recordException
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.safeRun
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.visible

object AnimationUtils {

    private const val ALPHA = "alpha"
    private const val SCALE_X = "scaleX"
    private const val SCALE_Y = "scaleY"
    private const val BACKGROUND_COLOR = "backgroundColor"

    fun View.animateGone(
        duration: Long = 250L,
        type: CustomAnimation,
        doOnEnd: ((View) -> Unit)? = null
    ) {
        var animatorA: ObjectAnimator? = null
        var animatorB: ObjectAnimator? = null
        when (type) {
            CustomAnimation.ALPHA -> {
                animatorA = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f)
                animatorA?.interpolator = FastOutLinearInInterpolator()
            }

            CustomAnimation.SCALE -> {
                animatorA = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_X,
                    1f,
                    0f
                ).apply {
                    interpolator = AnticipateInterpolator()
                }
                animatorB = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_Y,
                    1f,
                    0f
                ).apply {
                    interpolator = AnticipateInterpolator()
                }
            }

            CustomAnimation.TRANSLATION -> {
            }

            CustomAnimation.ROTATION -> {
            }
        }
        animatorA?.duration = duration
        animatorB?.duration = duration
        animatorA?.doOnEnd {
            // do on Up
            gone()
            if (doOnEnd != null) {
                doOnEnd(this)
            }
        }
        animatorB?.start()
        animatorA?.start()
    }

    fun View.animateVisible(
        duration: Long = 250L,
        type: CustomAnimation,
        doOnEnd: ((View) -> Unit)? = null
    ) {
        var animatorA: ObjectAnimator? = null
        var animatorB: ObjectAnimator? = null
        when (type) {
            CustomAnimation.ALPHA -> {
                alpha = 0f
                animatorA = ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f)
                animatorA?.interpolator = FastOutLinearInInterpolator()
            }

            CustomAnimation.SCALE -> {
                scaleX = 0f
                scaleY = 0f
                animatorA = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_X,
                    0f,
                    1f
                )
                animatorA?.apply { ->
                    interpolator = OvershootInterpolator()
                }
                animatorB = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_Y,
                    0f,
                    1f
                )
                animatorB.apply {
                    interpolator = OvershootInterpolator()
                }
            }

            CustomAnimation.TRANSLATION -> {
                //TODO: TRANSLATION ANIMATION WHILE TURNING VIEW VISIBLE
            }

            CustomAnimation.ROTATION -> {
                //TODO: Rotation ANIMATION WHILE TURNING VIEW VISIBLE
            }
        }
        visible()
        animatorA?.duration = duration
        animatorB?.duration = duration
        animatorA?.doOnEnd {
            // do on Up
            visible()
            if (doOnEnd != null) {
                doOnEnd(this)
            }
        }
        animatorA?.start()
        animatorB?.start()
    }


    private fun View.getAnimatorsForType(
        type: CustomAnimation,
        startValue: Float,
        endValue: Float
    ): List<ObjectAnimator> {
        when (type) {
            CustomAnimation.ALPHA -> {
                return listOf(
                    ObjectAnimator.ofFloat(this, View.ALPHA, startValue, endValue)
                )
            }
            CustomAnimation.SCALE -> {
                return listOf(
                    ObjectAnimator.ofFloat(
                        this,
                        View.SCALE_X,
                        startValue,
                        endValue
                    ),
                    ObjectAnimator.ofFloat(
                        this,
                        View.SCALE_Y,
                        startValue,
                        endValue
                    )
                )
            }
            CustomAnimation.TRANSLATION -> {
                return listOf(
                    ObjectAnimator.ofFloat(
                        this,
                        View.TRANSLATION_X,
                        startValue,
                        endValue
                    ),
                )
            }
            CustomAnimation.ROTATION -> {
                return listOf(
                    ObjectAnimator.ofFloat(
                        this,
                        View.ROTATION,
                        startValue,
                        endValue
                    ),
                )
            }
        }
    }


    /**Animate the view to be invisible
     * @param duration : Duration of the animation
     * @param type : Type of animation to be performed
     * @param doOnEnd : Action to be performed on the end of the animation
     * */
    fun View.animateInvisible(
        duration: Long = 500L,
        type: CustomAnimation,
        doOnEnd: (View) -> Unit
    ) {
        var animatorA: ObjectAnimator? = null
        var animatorB: ObjectAnimator? = null
        when (type) {
            CustomAnimation.ALPHA -> {
                animatorA = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f)
                animatorA?.interpolator = FastOutLinearInInterpolator()
            }

            CustomAnimation.SCALE -> {
                scaleX = 0f
                scaleY = 0f
                animatorA = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_X,
                    1f,
                    0f
                )
                animatorA?.apply { ->
                    interpolator = AnticipateInterpolator()
                }
                animatorB = ObjectAnimator.ofFloat(
                    this,
                    View.SCALE_Y,
                    1f,
                    0f
                )
                animatorB.apply {
                    interpolator = AnticipateInterpolator()
                }
            }

            CustomAnimation.TRANSLATION -> {
            }

            CustomAnimation.ROTATION -> {
            }
        }
        animatorA?.duration = duration
        animatorB?.duration = duration
        animatorA?.doOnEnd {
            // do on Up
            invisible()
            doOnEnd(this)
        }
        animatorA?.start()
        animatorB?.start()
    }

    /**Animate the view with a continuous alpha animation
     * @param repeatCount : Number of times the animation should repeat, 0 for infinite
     * @param duration : Duration of the animation
     * @param isSingular : If the color should also be animated
     * */
    fun View.animateContinuousAlpha(
        repeatCount: Int, duration: Long = 1000L,
        isSingular: Boolean = false
    ) {
        try {
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                this,
                //Alpha Value, Can also be Replaced with SCALE.
                PropertyValuesHolder.ofFloat(ALPHA, 0.3f),
                //Color Changing Property, UnComment to make Background Color Animate.
                /* PropertyValuesHolder.ofInt("backgroundColor",
                     resources.getColor(R.color.darkershimmer),
                     resources.getColor(R.color.shimmer2))*/
            )
            scaleDown.duration = duration
            if (repeatCount == 0) {
                scaleDown.repeatMode = ValueAnimator.REVERSE
                scaleDown.repeatCount = ValueAnimator.INFINITE
            } else if (repeatCount > 1) {
                scaleDown.repeatMode = ValueAnimator.REVERSE
                scaleDown.repeatCount = repeatCount
            }
            scaleDown.doOnRepeat {
                if (isSingular) {
                    it.cancel()
                    alpha = 1f
                    visible()
                }
            }
            scaleDown.doOnEnd {
                alpha = 1f
                visible()
            }
            scaleDown.start()
        } catch (e: Exception) {
            e.recordException()
        }
    }

    /**Animate the view with a continuous scale animation
     * @param repeatCount : Number of times the animation should repeat, 0 for infinite
     * @param duration : Duration of the animation
     * @param animateColor : If the color should also be animated - [Not Working]
     * */
    fun View.animateContinuousScale(
        repeatCount: Int, duration: Long = 1000L,
        animateColor: Boolean = false
    ){
        safeRun {
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                this,
                //Alpha Value, Can also be Replaced with SCALE.
                PropertyValuesHolder.ofFloat(SCALE_X, 1.2f),
                //Color Changing Property, UnComment to make Background Color Animate.
                /* PropertyValuesHolder.ofInt("backgroundColor",
                     resources.getColor(R.color.darkershimmer),
                     resources.getColor(R.color.shimmer2))*/
            )
            scaleDown.duration = duration
            if (repeatCount == 0) {
                scaleDown.repeatMode = ValueAnimator.REVERSE
                scaleDown.repeatCount = ValueAnimator.INFINITE
            } else if (repeatCount > 1) {
                scaleDown.repeatMode = ValueAnimator.REVERSE
                scaleDown.repeatCount = repeatCount
            }
            scaleDown.doOnRepeat {
                if (animateColor) {
                    it.cancel()
                    scaleX = 1f
                    visible()
                }
            }
            scaleDown.doOnEnd {
                scaleX = 1f
                visible()
            }
            scaleDown.start()
        }
    }

}