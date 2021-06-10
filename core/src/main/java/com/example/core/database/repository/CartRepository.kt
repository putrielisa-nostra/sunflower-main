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

import com.example.core.database.dao.CartDao
import com.example.core.database.entity.Cart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    suspend fun createCart(plantId: String, total: Int, price: Float) {
        val myCart = Cart(plantId, total, price)
        cartDao.insertItemCart(myCart)
    }

    suspend fun removeHarvestPlanting(myCart: Cart) {
        cartDao.removeItemCart(myCart)
    }

    fun getlistCart() = cartDao.getharvestCart()

    fun getCartByPlant(plantId: String) {
        cartDao.getCartByPlant(plantId)
    }
}
