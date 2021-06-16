/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.core.network

import com.example.core.database.entity.FoodResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface FoodService {

    @GET("api/json/v1/1/search.php?f=f")
    suspend fun fetchFoodList(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): ApiResponse<FoodResponse>

    @FormUrlEncoded
    @POST("save")
    suspend fun sendNotification (
        @Field("name") name: String,
        @Field("token") token: String)
            : Response<AuthResponse>

    //@GET("meals/{name}")
    //suspend fun fetchFoodInfo(@Path("name") name: String): ApiResponse<FoodInfo>
}
