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
import com.example.core.database.entity.HarvestPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM item_cart A " +
            "LEFT JOIN meals B ON A.item_id = B.idMeal " +
            "WHERE A.item_id = :itemID")
    fun getItemByID(itemID: String): Flow<Cart>

    @Query("SELECT * FROM item_cart")
    fun getAllItemCart(): List<Cart>

    @Insert
    suspend fun insertItemCart(cartItem: Cart): Long

    @Delete
    suspend fun removeItemCart(cartItem: Cart)
}
