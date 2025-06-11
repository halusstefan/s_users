package com.slide.test.network.service

import com.slide.test.network.model.CreateUserRequestDto
import com.slide.test.network.model.ListResponseDto
import com.slide.test.network.model.PostDto
import com.slide.test.network.model.ResponseDto
import com.slide.test.network.model.UserDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Stefan Halus on 18 May 2022
 */

interface UsersService {

    @GET("public-api/users")
    fun fetchUsers(@Query("page") page: Long?): Single<ListResponseDto<UserDto>>

    @DELETE("public-api/users/{userId}")
    fun deleteUser(@Path("userId") userId: Long): Completable

    @POST("public-api/users")
    fun createUser(@Body createRequestDto: CreateUserRequestDto): Single<ResponseDto<UserDto>>

    @GET("public-api/users/{userId}/posts")
    suspend fun getUserPosts(@Path("userId") userId: Long): ResponseDto<List<PostDto>>
}