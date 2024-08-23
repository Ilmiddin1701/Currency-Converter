package com.example.valutaconverterapp.Retrofit

import com.example.valutaconverterapp.Models.Courses
import retrofit2.http.GET

interface ApiService {
    @GET("json")
    suspend fun getCourses(): List<Courses>
}