package com.xs.carappinterviewtask.data.source

import com.xs.carappinterviewtask.data.api.CarsApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private  val apiService: CarsApiService) {
    suspend fun getCars() = apiService.getCars()
}