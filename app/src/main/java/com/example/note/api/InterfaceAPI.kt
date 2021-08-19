package com.example.myapplication.api

import com.example.note.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceAPI {
    @GET("api/?results=15&noinfo")
    fun getPersonsJson():Call<ApiResponse>
}