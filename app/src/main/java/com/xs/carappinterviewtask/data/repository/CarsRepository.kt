package com.xs.carappinterviewtask.data.repository

import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import com.xs.carappinterviewtask.utils.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CarsRepository {
    fun getCars(): Flow<NetworkResult<GetCarsResponse>>
}