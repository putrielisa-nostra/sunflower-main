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

package com.google.samples.apps.sunflower.viewmodels

import com.google.samples.apps.sunflower.data.GardenAndHarvest
import java.text.SimpleDateFormat
import java.util.*

class GardenAndHarvestViewModel(harvests: GardenAndHarvest) {

    private val garden = checkNotNull(harvests.garden)
    private val plant = checkNotNull(harvests.plant)
    private val harvest = harvests.harvestGarden[0]
    val harvestDateString: String = harvest.harvest_date
    val imageUrl
        get() = plant.imageUrl
    val plantName
        get() = plant.name
    val amountHarvest: String = harvest.harvest_amount.toString()
    val plantId
        get() = garden.plantId

    companion object {
        private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}