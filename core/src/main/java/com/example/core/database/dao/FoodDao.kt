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

package com.example.core.database.dao

import androidx.room.*
import com.example.core.database.entity.Cart
import com.example.core.database.entity.Food
import com.example.core.database.entity.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodList(foodList: List<Food>)

    @Delete
    suspend fun removeFood(food: Food)

    @Query("SELECT * FROM meals WHERE page = :page_")
    suspend fun getFoodList(page_: Int): List<Food>

    @Query("SELECT * FROM meals WHERE page = :page_")
    suspend fun getAllFoodList(page_: Int): List<Food>

    @Query("SELECT * FROM meals WHERE idMeal = :foodID")
    fun getFoodByID(foodID: String): Flow<Food>
}
