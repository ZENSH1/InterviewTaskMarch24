package com.xs.carappinterviewtask.ui.screens.splashScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xs.carappinterviewtask.R
import com.xs.carappinterviewtask.data.enums.CustomAnimation
import com.xs.carappinterviewtask.databinding.FragmentSplashBinding
import com.xs.carappinterviewtask.utils.base.BaseFragment
import com.xs.carappinterviewtask.utils.objects.AnimationUtils.animateGone
import java.util.concurrent.atomic.AtomicBoolean

class SplashFragment : BaseFragment<FragmentSplashBinding>(false) {
    private var shouldSkip = AtomicBoolean(false)
    override fun layoutResource(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater,container,false)
    }

    override fun onBackPressedCallback() {
        mainActivity.moveTaskToBack(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.splashIcon.animateGone(3000L,CustomAnimation.SCALE){
            navigateTo(R.id.splashFragment,R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if(shouldSkip.getAndSet(true)){
            navigateTo(R.id.splashFragment,R.id.action_splashFragment_to_homeFragment)
        }

    }


}