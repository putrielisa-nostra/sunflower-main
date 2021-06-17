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

package com.example.core.database.repository

import androidx.annotation.WorkerThread
import com.example.core.database.dao.FoodDao
import com.example.core.database.entity.Food
import com.example.core.network.utilities.ErrorResponseMapper
import com.example.core.network.FoodClient
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import com.skydoves.sandwich.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodClient: FoodClient,
    private val foodDao: FoodDao
) : Repository {

    @WorkerThread
    fun fetchFoodList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var foods = foodDao.getAllFoodList(page)
        if (foods.isEmpty()) {

            val response = foodClient.fetchDataFromService(page = page)
            response.suspendOnSuccess {
                data.whatIfNotNull { response ->
                    foods = response.meals
                    foods.forEach { food -> food.page = page }
                    foodDao.insertFoodList(foods)
                    emit(foodDao.getAllFoodList(page))
                }
            }
                // handles the case when the API request gets an error response.
                // e.g., internal server error.
                .onError {
                    /** maps the [ApiResponse.Failure.Error] to the [NetworkErrorResponse] using the mapper. */
                    map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
                }
                // handles the case when the API request gets an exception response.
                // e.g., network connection error.
                .onException { onError(message) }
        } else {
            emit(foodDao.getAllFoodList(page))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    suspend fun removeFood(food: Food) = foodDao.removeFood(food)
    fun getFoodByID(foodID: String) = foodDao.getFoodByID(foodID)
}
