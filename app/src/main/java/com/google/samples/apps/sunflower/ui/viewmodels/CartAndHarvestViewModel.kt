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

import com.google.samples.apps.sunflower.data.CartAndHarvest

class CartAndHarvestViewModel(cart: CartAndHarvest) {

    private val harvest = checkNotNull(cart.harvest)
    private val plant = checkNotNull(cart.plant)
    private val cart_ = cart.cartHarvest[0]
    val cartDate: String = cart_.cart_date.toString()
    val imageUrl
        get() = plant.imageUrl
    val plantName
        get() = plant.name
    val totalItem: String = cart_.item_total.toString()
    val plantId
        get() = harvest.harvest_plant_id

}