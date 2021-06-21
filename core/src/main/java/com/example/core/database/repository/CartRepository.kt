/*
 * Copyright 202 1 Google LLC
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

import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.core.database.dao.CartDao
import com.example.core.database.entity.Cart
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) : Repository {

    fun getItemByID(itemID: String): Cart = cartDao.getItemByID(itemID)

    fun getAllItem() = cartDao.getAllItemCart()

    fun isCart(): Boolean {
        val list = cartDao.getAllItemCart().asLiveData()
        if (list.value?.size ?: 0 > 0) {
            return true
        } else {
            return false
        }
    }

    suspend fun createCart(foodId: String, total: Int, price:String) {
        val cart = Cart(foodId, total, price)
        cartDao.insertItemCart(cart)
    }

    fun isCart(foodId: String) =
        cartDao.isCart(foodId)

    fun UpdateItemCart(foodId: String, total: Int) {
        try {
            cartDao.UpdateItemCart(foodId, total)
        }
        catch (ex: Exception) {
            Log.e("Cart-Repo: ", ex.toString())
        }
    }

    suspend fun RemoveItemCart(foodId: String, total: Int, price:String) {
        val cart = Cart(foodId, total, price)
        cartDao.removeItemCart(cart)
    }
}
