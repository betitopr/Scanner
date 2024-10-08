package com.example.scanner

import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("product/{barcode}.json")
    suspend fun getProductInfo(@Path("barcode") barcode: String): OpenFoodFactsResponse
}