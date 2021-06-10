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

package com.google.samples.apps.sunflower.ui.viewmodels

import com.example.core.database.entity.CartAndHarvest

class CartAndHarvestViewModel(carts: CartAndHarvest) {

    private val cartList = checkNotNull(carts.cartHarvest)
    private val plant = checkNotNull(carts.plant)
    val plantId
        get() = plant.plantId
    //var cart: Cart = cartList.filter { s -> s.cart_plant_id == plantId }.single()
    val itemTotal : String = cartList[0].item_total.toString() //= cart.item_total.toString()
    val imageUrl
        get() = plant.imageUrl
    val plantName
        get() = plant.name

}