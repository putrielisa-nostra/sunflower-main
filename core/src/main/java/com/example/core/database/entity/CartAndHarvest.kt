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

package com.example.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.core.database.entity.Cart
import com.example.core.database.entity.HarvestPlant
import com.example.core.database.entity.Plant

data class CartAndHarvest(
        @Embedded
        val plant: Plant,

        @Relation(parentColumn = "id", entityColumn = "harvest_plant_id")
        val harvestPlantings: List<HarvestPlant> = emptyList(),

        @Embedded
        val harvest: HarvestPlant,

        @Relation(parentColumn = "harvest_plant_id", entityColumn = "cart_plant_id")
        val cartHarvest: List<Cart> = emptyList()
)