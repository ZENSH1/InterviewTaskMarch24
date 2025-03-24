package com.xs.carappinterviewtask.ui.screens.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.xs.carappinterviewtask.R
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import com.xs.carappinterviewtask.databinding.FragmentHomeBinding
import com.xs.carappinterviewtask.ui.screens.homeScreen.adapter.CarsAdapter
import com.xs.carappinterviewtask.ui.viewmodels.SharedViewModel
import com.xs.carappinterviewtask.utils.base.BaseFragment
import com.xs.carappinterviewtask.utils.networkUtils.NetworkResult
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.collectLatestLifecycleFlow
import com.xs.carappinterviewtask.utils.objects.SharedTransitions
import com.xs.carappinterviewtask.utils.uiUtils.ApiErrorLayoutHandler.showError
import com.xs.carappinterviewtask.utils.uiUtils.ApiErrorLayoutHandler.showLoading
import com.xs.carappinterviewtask.utils.uiUtils.ApiErrorLayoutHandler.showSuccess

class HomeFragment : BaseFragment<FragmentHomeBinding>(true) {

    private val viewModel: SharedViewModel by activityViewModels()
    private val adapter by lazy {
        CarsAdapter(
            mutableListOf(),
            { item, layoutBinding ->
                viewModel.selectedItem = item
                navigateTo(R.id.homeFragment, R.id.action_homeFragment_to_detailsFragment)
            }
        )
    }

    override fun layoutResource(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onBackPressedCallback() {
        mainActivity.moveTaskToBack(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        observeData()
    }

    private fun setUpUi() {
        binding.carsRv.adapter = adapter
        binding.searchEt.addTextChangedListener {
            adapter.filter(it.toString())
        }
    }

    private fun observeData() {
        collectLatestLifecycleFlow(viewModel.carsflow) {
            when (it) {
                is NetworkResult.Error -> {
                    binding.errorLayout.showError(
                        it.message ?: "Unknown Error",
                        onRetry = { viewModel.getCars() },
                    )
                }

                is NetworkResult.Idle -> {
                    viewModel.getCars()
                }

                is NetworkResult.Loading -> {
                    binding.errorLayout.showLoading()
                }

                is NetworkResult.Success -> {
                    binding.errorLayout.showSuccess()
                    setUpStuff(it.result!!)
                }
            }
        }
    }


    private fun setUpStuff(result: GetCarsResponse) {
        adapter.updateData(result)
    }

}