package com.alpsproject.devicetracking.api

import com.alpsproject.devicetracking.model.SensorData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class API {

    fun sendSensorEntry(sensorData: SensorData, onResult: (SensorData?) -> Unit) {
        val retrofit = APIServiceBuilder.buildService(APIServices::class.java)
        retrofit.postSensorEntry(sensorData).enqueue(
            object : Callback<SensorData> {
                override fun onFailure(call: Call<SensorData>, t: Throwable) {
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