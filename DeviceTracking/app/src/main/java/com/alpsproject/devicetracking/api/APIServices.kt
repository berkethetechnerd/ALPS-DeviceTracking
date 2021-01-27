package com.alpsproject.devicetracking.api

import com.alpsproject.devicetracking.model.SensorData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServices {

    @Headers("Content-Type: application/json")
    @POST("/data")
    fun postSensorEntry(@Body sensorEntry: SensorData): Call<SensorData>

}