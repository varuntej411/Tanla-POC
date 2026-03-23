package com.ascendion.tanla.data.remote

import retrofit2.http.POST

interface ApiService {
    @POST("/report")
    suspend fun reportSpam()
}