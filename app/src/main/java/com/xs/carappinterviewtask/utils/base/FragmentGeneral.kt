package com.xs.carappinterviewtask.utils.base

import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.intuit.sdp.BuildConfig
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.recordException
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.safeRun


open class FragmentGeneral : Fragment() {

    private val baseTAG = "BaseTAG"

    protected fun withDelay(delay: Long = 1000, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(block, delay)
    }

    private fun getResString(stringId: Int): String {
        return context?.resources?.getString(stringId) ?: ""
    }

    /* ---------- Toast ---------- */

    protected fun showToast(message: String) {
        activity?.let {
            try {
                it.runOnUiThread {
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                    Log.e("TOAST", message)
                }
            } catch (ex: Exception) {
                ex.recordException()
            }
        }
    }

    protected fun debugToast(message: String) {
        if (BuildConfig.DEBUG) {
            showToast(message)
        }
    }

    protected fun showToast(stringId: Int) {
        val message = getResString(stringId)
        showToast(message)
    }

    /* ---------- Snackbar ---------- */

    protected fun showSnackBar(message: String) {
        this.view?.let { v ->
            activity?.let {
                try {
                    it.runOnUiThread {
                        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
                    }
                } catch (ex: Exception) {
                    ex.recordException()
                }
            }
        }
    }


    val hideKeyboardListener = View.OnTouchListener { v, event ->
        v?.requestFocus()
        false
    }

    /* ---------- Keyboard (Input Method) ---------- */

    protected fun showKeyboard(v: View) {
        safeRun {
            val imm: InputMethodManager? = context?.getSystemService(InputMethodManager::class.java)
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    protected fun hideKeyboard() {
        safeRun {
            val inputMethodManager: InputMethodManager =
                requireActivity().getSystemService(InputMethodManager::class.java)
            val view: IBinder =
                requireActivity().findViewById<View?>(android.R.id.content).windowToken
            inputMethodManager.hideSoftInputFromWindow(view, 0)
        }
    }

    protected fun hideKeyboard(view: View) {
        safeRun {
            val imm: InputMethodManager? = context?.getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}