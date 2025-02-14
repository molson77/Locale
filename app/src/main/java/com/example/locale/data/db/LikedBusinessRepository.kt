package com.example.locale.data.db

import com.example.locale.data.model.Business
import com.example.locale.data.model.BusinessDetails
import javax.inject.Inject

class LikedBusinessRepository @Inject constructor(
    private val likedBusinessDao: LikedBusinessDao
) {

    suspend fun getAll() = likedBusinessDao.getAll()

    suspend fun findBusiness(business: Business) = likedBusinessDao.findBusiness(business.id)

    suspend fun like(id: String) = likedBusinessDao.insert(LikedBusiness(id))

    suspend fun unlike(id: String) = likedBusinessDao.delete(LikedBusiness(id))

}