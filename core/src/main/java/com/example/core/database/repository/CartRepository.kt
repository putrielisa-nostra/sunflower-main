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
import com.example.core.database.dao.CartDao
import com.example.core.database.dao.FoodDao
import com.example.core.database.entity.Cart
import com.example.core.network.ErrorResponseMapper
import com.example.core.network.FoodClient
import com.skydoves.sandwich.*
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) : Repository {

    @WorkerThread
    fun getItemByID(itemID: String) = cartDao.getItemByID(itemID)
    @WorkerThread
    fun getAllItem(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var foods = cartDao.getAllItemCart()
            emit(cartDao.getAllItemCart())

    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    //fun getAllItem() = cartDao.getAllItemCart()
}
