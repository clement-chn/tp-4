package com.ynov.showroom

import retrofit2.http.GET

interface CarApi {
    @GET("tc-test-ios.json")
    suspend fun getCars(): List<Car>
}
