package com.xs.carappinterviewtask.utils.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.addCallback
import androidx.core.graphics.Insets
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.xs.carappinterviewtask.MainActivity
import com.xs.carappinterviewtask.utils.base.BaseNavFragment
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.safeReturn
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.windowInsets

abstract class BaseFragment<T : ViewBinding>(private val backPressEnabled: Boolean) :
    BaseNavFragment() {

    private var _binding: T? = null

    val binding
        get() = _binding!!

    /** - Global Variables that will be used in the fragment
     *  - these are the most common variables that are used in every fragment. and therefore
     *    are created in the base fragment to alleviate having to set them up in every fragment
     * */
    val globalContext: Context by lazy { safeReturn(requireContext()) { binding.root.context } }
    private val globalActivity by lazy { globalContext as Activity }
    val mainActivity by lazy { globalActivity as MainActivity }

    /** - Obtains the layout binding for the fragment.
     *  - it is called within the on create view function, so the binding can be set up.
     *  makes it easier to setup the view binding in the fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @return T of type ViewBinding
     * @author Syed Zain Sherazi - 622
     * */
    abstract fun layoutResource(inflater: LayoutInflater, container: ViewGroup?): T

    /** - Handles the back pressed callback
     *  - it is called when the back button  is pressed or when swiped off the screen in the fragment.
     *  @author Syed Zain Sherazi - 622
     */
    abstract fun onBackPressedCallback()

    /**if u want to do something before returning the binding. it will be done here.
     * @author Syed Zain Sherazi - 622
     * */
    open fun onCreateView() {

    }

    /** Handles System Window Insets, so we can adjust where to set those insets in the fragment.
     * By default the insets are padded to the root of fragment.
     * @param insets Insets
     * @author Syed Zain Sherazi - 622
     *
     * */
    open fun handleInsets(insets: Insets) {
        binding.root.setPadding(insets.left, insets.top, insets.right, insets.bottom)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeReturn {
            val animation = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
            animation?.duration = 350L
            animation?.interpolator = AccelerateDecelerateInterpolator()
            sharedElementEnterTransition = animation
            sharedElementReturnTransition = animation
        }
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * This method is responsible for inflating the layout and initializing
     * the binding object. It also handles any window insets if available.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                 any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to. The fragment should not
     *                  add the view itself, but this can be used to generate
     *                  the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being
     *                           re-constructed from a previous saved state.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = layoutResource(inflater, container)
        safeReturn { onCreateView() }
        windowInsets?.let {
            handleInsets(it)
        }
        return binding.root
    }

    /**
     * Called when the view hierarchy associated with the fragment is created.
     * This is where you can perform initialization tasks, such as setting up
     * listeners or preparing data for the view.
     *
     * In this implementation, we check if back press handling is enabled.
     * If it is, we add a callback to the back press dispatcher to handle
     * back press events. The callback will only execute if the stopCallback
     * flag in the main activity is false.
     *
     * @param view The view returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (backPressEnabled) {
            mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
                onBackPressedCallback()
            }
        }
    }

    /**
     * Called when the view hierarchy associated with the fragment is being destroyed.
     * This is the place to clean up any resources related to the view.
     *
     * In this implementation, we set the binding reference to null to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}