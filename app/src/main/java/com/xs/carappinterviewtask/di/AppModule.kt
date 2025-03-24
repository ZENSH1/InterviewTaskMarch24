package com.xs.carappinterviewtask.di

import com.google.gson.Gson
import com.xs.carappinterviewtask.data.api.CarsApiService
import com.xs.carappinterviewtask.data.repository.CarsRepository
import com.xs.carappinterviewtask.data.repository.CarsRepositoryImpl
import com.xs.carappinterviewtask.data.source.RemoteDataSource
import com.xs.carappinterviewtask.utils.AppConsts.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideHTTPClient(
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): GsonConverterFactory = GsonConverterFactory
        .create(Gson().newBuilder().create())

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
    }


    @Singleton
    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): CarsApiService = retrofitBuilder.client(okHttpClient).build().create(CarsApiService::class.java)

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        apiService: CarsApiService,
    ): RemoteDataSource = RemoteDataSource(apiService)


    @Singleton
    @Provides
    fun provideCarsRepository(
        remoteDataSource: RemoteDataSource
    ): CarsRepository = CarsRepositoryImpl(remoteDataSource)


}