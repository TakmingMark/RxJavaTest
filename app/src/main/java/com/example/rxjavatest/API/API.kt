package com.example.rxjavatest.API

import com.example.rxjavatest.Model.Albums
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

interface API {

    @GET("albums/1")
    fun getAlbums(): Observable<Albums>

    @GET("albums/{id}")
    fun getAlbumsById(@Path("id") id: Int)

    @POST("albums")
    fun postAlbums(@Body albums: Albums): Observable<Albums>
}