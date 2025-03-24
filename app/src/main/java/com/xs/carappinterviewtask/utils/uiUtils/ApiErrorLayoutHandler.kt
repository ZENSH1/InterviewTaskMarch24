package com.xs.carappinterviewtask.utils.uiUtils

import com.xs.carappinterviewtask.R
import com.xs.carappinterviewtask.databinding.ApiErrorLayoutBinding
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.gone
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.log
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.safeRun
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.setDebounceListener
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.visible

object ApiErrorLayoutHandler {
    fun ApiErrorLayoutBinding.showError(message:String, onRetry:()->Unit){
        safeRun{
            root.visible()
            progressCircular.gone()
            errorStateImage.visible()
        }
        safeRun{
            root.setBackgroundResource(R.drawable.ic_rounded_bg)
        }
        safeRun{
            tvErrorMessage.visible()
            retryBtn.visible()
            tvErrorMessage.text = message
        }
        this@showError.retryBtn.setDebounceListener() {
            "Calling Retry".log("Api")
            onRetry.invoke()
        }
    }

    fun ApiErrorLayoutBinding.showLoading(){
        root.visible()
        safeRun{
            root.setBackgroundResource(0)
        }
        errorStateImage.gone()
        tvErrorMessage.gone()
        retryBtn.gone()
        progressCircular.visible()
    }

    fun ApiErrorLayoutBinding.showSuccess(){
        root.gone()
    }

}