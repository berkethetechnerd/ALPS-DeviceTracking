package com.alpsproject.devicetracking.api

import com.alpsproject.devicetracking.helper.CalendarManager
import com.alpsproject.devicetracking.helper.ConstantsManager
import com.alpsproject.devicetracking.helper.Logger
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import com.alpsproject.devicetracking.model.SensorData
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object SensorMetricsAPI {

    fun sendSensorEntry(sensorData: SensorData, onResult: (SensorData?) -> Unit) {
        val retrofit = APIServiceBuilder.buildService(APIServices::class.java)
        val jsonSensorData = getJsonObject(sensorData)

        retrofit.postSensorEntry(jsonSensorData).enqueue(
            object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    onResult(null) // On failure, returns null
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Logger.logAPINotification()
                    onResult(sensorData) // On success, returns the same object
                }
            }
        )
    }

    private fun getJsonObject(sensorData: SensorData) = JsonObject().apply {
        addProperty("device_id", SharedPreferencesManager.read(ConstantsManager.DEVICE_IDENTIFIER, ConstantsManager.DEVICE_IDENTIFIER_DEFAULT))
        addProperty("sensor_name", sensorData.sensorName)
        addProperty("start_time", CalendarManager.dateToBackendString(sensorData.startTime))
        addProperty("end_time", CalendarManager.dateToBackendString(sensorData.endTime))
    }
}

private object APIServiceBuilder {

    private val retrofit: Retrofit
    private val address: String
        get() = SharedPreferencesManager.read(
                ConstantsManager.DEFAULT_API,
                ConstantsManager.getDefaultAPIURL()
        ) ?: ConstantsManager.getDefaultAPIURL()

    init {
        val client = OkHttpClient.Builder().build()

        retrofit = Retrofit.Builder()
            .baseUrl(address)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

private interface APIServices {

    @Headers("Content-Type: application/json")
    @POST("/data")
    fun postSensorEntry(@Body sensorEntry: JsonObject): Call<JsonObject>

}