package com.example.locale.network

import com.example.locale.data.model.BusinessDetails
import com.example.locale.data.model.BusinessDetailsResponse
import com.example.locale.data.model.BusinessSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpFusionService {
    @GET("businesses/search")
    suspend fun getBusinessesByLocationName(
        @Query("location") location : String,
        @Query("limit") limit : Int = 10
    ) : Response<BusinessSearchResponse>

    @GET("businesses/{id}")
    suspend fun getBusinessDetails(
        @Path("id") id : String
    ) : Response<BusinessDetails>
}