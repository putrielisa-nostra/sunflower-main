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

import com.example.core.database.dao.HarvestPlantingDao
import com.example.core.database.entity.HarvestPlant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HarvestPlantRepository @Inject constructor(
    private val harvestPlantingDao: HarvestPlantingDao
) {

    suspend fun createHarvestPlanting(plantId: String, total: Int) {
        val gardenPlanting = HarvestPlant(plantId, total)
        harvestPlantingDao.insertHarvestPlant(gardenPlanting)
    }
    suspend fun updateHarvestPlanting(plantId: String, total: Int) {
        harvestPlantingDao.updateHarvest(plantId, total)
    }

    suspend fun removeHarvestPlanting(harvestPlanting: HarvestPlant) {
        harvestPlantingDao.deleteHarvestPlant(harvestPlanting)
    }
    fun isHarvest(plantId: String):Boolean =
        harvestPlantingDao.isHarvest(plantId)

    fun getOneListHarvest() = harvestPlantingDao.getOneListHarvest()
    fun getHarvestByPlant(plantId: String)=
            harvestPlantingDao.getHarvestByPlant(plantId)
}
