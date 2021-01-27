package com.alpsproject.devicetracking.api

import com.alpsproject.devicetracking.helper.CalendarManager
import com.alpsproject.devicetracking.helper.Logger
import com.alpsproject.devicetracking.model.SensorData
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_TEST = "http://192.168.0.3:8080"
private const val BASE_URL_LIVE = ""

object API {

    fun sendSensorEntry(sensorData: SensorData, onResult: (SensorData?) -> Unit) {
        val retrofit = APIServiceBuilder.buildService(APIServices::class.java)
        retrofit.postSensorEntry(getJsonObject(sensorData)).enqueue(
            object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Logger.logAPINotification()
                    onResult(sensorData)
                }
            }
        )
    }

    private fun getJsonObject(sensorData: SensorData): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("sensor_name", sensorData.sensorName)
        jsonObject.addProperty("start_time", CalendarManager.convertDateForBackend(sensorData.startTime))
        jsonObject.addProperty("end_time", CalendarManager.convertDateForBackend(sensorData.endTime))

        return jsonObject
    }
}

object APIServiceBuilder {

    private val retrofit: Retrofit

    init {
        val client = OkHttpClient.Builder().build()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_TEST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}