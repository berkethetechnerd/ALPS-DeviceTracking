package com.alpsproject.devicetracking.api

import android.util.Log
import com.alpsproject.devicetracking.model.SensorData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_TEST = "http://192.168.0.2"
private const val BASE_URL_LIVE = ""

object API {

    fun sendSensorEntry(sensorData: SensorData, onResult: (SensorData?) -> Unit) {
        val retrofit = APIServiceBuilder.buildService(APIServices::class.java)
        retrofit.postSensorEntry(sensorData).enqueue(
            object : Callback<SensorData> {
                override fun onFailure(call: Call<SensorData>, t: Throwable) {
                    val msg = t.message
                    if (msg.isNullOrEmpty()) {
                        Log.d("SensorDataEntry", "nulnul")
                    } else {
                        Log.d("SensorDataEntry", msg)
                    }

                    onResult(null)
                }

                override fun onResponse(call: Call<SensorData>, response: Response<SensorData>) {
                    val sentData = response.body()
                    onResult(sentData)
                }
            }
        )
    }
}

object APIServiceBuilder {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_TEST)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}