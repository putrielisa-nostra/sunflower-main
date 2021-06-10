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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.database.entity.Cart
import com.example.core.database.entity.CartAndHarvest
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_plants")
    fun getAllCart(): Flow<List<Cart>>

    @Query("SELECT * FROM cart_plants A " +
            "LEFT JOIN harvest_plantings B ON A.cart_plant_id = B.harvest_plant_id " +
            "where cart_plant_id=:plantId LIMIT 1")
    fun getCartByPlant(plantId:String): Flow<Cart>

    @Transaction
    @Query("SELECT * FROM cart_plants A " +
            "LEFT JOIN harvest_plantings B ON A.cart_plant_id = B.harvest_plant_id " +
            "LEFT JOIN plants C ON A.cart_plant_id = C.id")
    fun getharvestCart(): Flow<List<CartAndHarvest>>

    @Insert
    suspend fun insertItemCart(cartPlant: Cart): Long

    @Delete
    suspend fun removeItemCart(cartPlant: Cart)
}
