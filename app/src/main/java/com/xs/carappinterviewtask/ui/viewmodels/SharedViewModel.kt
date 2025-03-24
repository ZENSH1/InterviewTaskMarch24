package com.xs.carappinterviewtask.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponseItem
import com.xs.carappinterviewtask.data.repository.CarsRepository
import com.xs.carappinterviewtask.utils.networkUtils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val carsRepository: CarsRepository
): ViewModel() {
    var selectedItem: GetCarsResponseItem? = null
    private val _carsflow = MutableStateFlow<NetworkResult<GetCarsResponse>>(NetworkResult.Idle())
    val carsflow = _carsflow.asStateFlow()

    fun getCars(){
        viewModelScope.launch {
            carsRepository.getCars().collectLatest{
                _carsflow.value = it
            }

        }
    }

}