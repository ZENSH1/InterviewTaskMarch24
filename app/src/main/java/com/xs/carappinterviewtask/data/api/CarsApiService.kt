package com.xs.carappinterviewtask.data.api

import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import retrofit2.Response
import retrofit2.http.GET

interface CarsApiService {
    @GET("cars")
    suspend fun getCars(): Response<GetCarsResponse>
}