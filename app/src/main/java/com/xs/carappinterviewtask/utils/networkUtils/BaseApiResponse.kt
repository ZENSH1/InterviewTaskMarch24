package com.xs.carappinterviewtask.utils.networkUtils

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.google.gson.Gson
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponse
import com.xs.carappinterviewtask.data.model.shared.ErrorResponse
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.log
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.recordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

object BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(Dispatchers.IO){
            try {
                val response = apiCall()
                //if response is Success
                if (response.isSuccessful) {
                    val body = response.body()
                    "SafeAPICall : body -> ${response.body().toString()}".log(TAG)
                    //Sending Body as Success if body isn't null
                    body?.let {
                        return@withContext NetworkResult.Success(body)
                    }
                }
                //Error String
                val errorMsg = response.errorBody()?.string()
                response.errorBody()?.close()
                errorMsg?.log(TAG)
                val errorResponse = try {
                    Gson().fromJson<ErrorResponse>(errorMsg, ErrorResponse::class.java)
                }catch (e:Exception){
                    ErrorResponse(message = errorMsg?:"Api Call Failed!","App Exception",900, errors = null)
                }
                return@withContext error((errorResponse.errors?.firstOrNull()?:errorResponse.message),response.body())
            } catch (e: Exception) {
                e.recordException(TAG)

                // Check for internet connection error
                if (e is java.net.UnknownHostException || e is java.net.SocketException) {
                    return@withContext error("Check Your Internet Connection!")
                }
                return@withContext error("Api call failed ${e.message ?: e.toString()}")
            }
        }
    }

    private fun <T> error(errorMessage: String, body: T? = null): NetworkResult<T> =
        NetworkResult.Error("$errorMessage", body)

    private const val TAG = "API"

}
