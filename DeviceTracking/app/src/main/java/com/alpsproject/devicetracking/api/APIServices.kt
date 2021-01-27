package com.alpsproject.devicetracking.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServices {

    @Headers("Content-Type: application/json")
    @POST("/data")
    fun postSensorEntry(@Body sensorEntry: JsonObject): Call<JsonObject>

}