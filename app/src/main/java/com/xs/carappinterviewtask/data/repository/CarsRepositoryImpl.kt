package com.xs.carappinterviewtask.data.repository

import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import com.xs.carappinterviewtask.data.source.RemoteDataSource
import com.xs.carappinterviewtask.utils.networkUtils.BaseApiResponse.safeApiCall
import com.xs.carappinterviewtask.utils.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CarsRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : CarsRepository {
    override fun getCars(): Flow<NetworkResult<GetCarsResponse>> = flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.getCars() })
    }
}