package com.xs.carappinterviewtask.utils.base

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

abstract class BaseNavFragment : FragmentGeneral() {


    open fun navIconBackPressed() {
        onBackPressed()
    }

    open fun onBackPressed() {
//        findNavController().currentDestination?.let { popFrom(it.orderId) }
    }

    val singleTop by lazy { NavOptions.Builder().setLaunchSingleTop(true).build() }
    val popUpTo by lazy { NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build() }
    /**
     *     Used launchWhenCreated, bcz of screen rotation
     *     Used launchWhenResumed, bcz of screen rotation
     * @param fragmentId : Current Fragment's Id (from Nav Graph)
     * @param action : Action / Id of other fragment
     * @param bundle : Pass bundle as a NavArgs to destination.
     * @param extras : FragmentNavigator.Extras for Shared Element Transition
     */
    protected fun navigateTo(fragmentId: Int, action: Int, bundle: Bundle) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action, bundle)
            }
        }
    }
    protected fun navigateTo(fragmentId: Int, action: Int) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action)
            }
        }
    }

    protected fun navigateTo(fragmentId: Int,action: Int,args:Bundle? = null,navOptions: NavOptions? = null,extras: FragmentNavigator.Extras? = null){
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)){
                findNavController().navigate(action,args,navOptions,extras)
            }
        }
    }
    protected fun navigateTo(fragmentId: Int, action: Int,navOptions: NavOptions) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action,null,navOptions)
            }
        }
    }

    protected fun navigateTo(fragmentId: Int, action: NavDirections) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action)
            }
        }
    }

    protected fun navigateUp() {
        launchWhenCreated {
            findNavController().navigateUp()
        }
    }


    protected fun popFrom(fragmentId: Int) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack()
            }
        }
    }

    protected fun popFrom(fragmentId: Int, destinationFragmentId: Int, inclusive: Boolean = false) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack(destinationFragmentId, inclusive)
            }
        }
    }

    private fun isCurrentDestination(fragmentId: Int): Boolean {
        return findNavController().currentDestination?.id == fragmentId
    }

    private fun launchWhenCreated(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withCreated(callback) }
    }

    protected fun launchWhenStarted(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withStarted(callback) }
    }

    protected fun launchWhenResumed(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withResumed(callback) }
    }
}