package com.hrishi.tempmail

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TempMailApi {

    @GET("api/v1/?action=genRandomMailbox")
    suspend fun generateMailId() : Response<Array<String>>

    @GET("api/v1/?action=getMessages")
    suspend fun getallMail(@Query("login") login : String
                           ,@Query("domain") domain : String) : Response<Array<MailPreview>>

    @GET("api/v1/?action=readMessage")
    suspend fun getMailDetails(@Query("login") login : String
                           ,@Query("domain") domain : String, @Query("id") id : String) : Response<Array<Mail>>
}