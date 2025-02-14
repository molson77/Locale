package com.example.locale.network

import com.example.locale.data.model.BusinessDetails
import com.example.locale.data.model.BusinessDetailsResponse
import com.example.locale.data.model.BusinessSearchResponse
import retrofit2.Response
import javax.inject.Inject

class YelpFusionRepository @Inject constructor(
    private val yelpFusionService: YelpFusionService
) {

    suspend fun getBusinessesByLocationName(
        location: String
    ) : Response<BusinessSearchResponse> {
        return yelpFusionService.getBusinessesByLocationName(location)
    }

    suspend fun getBusinessDetails(
        id: String
    ) : Response<BusinessDetails> {
        return yelpFusionService.getBusinessDetails(id)
    }

}