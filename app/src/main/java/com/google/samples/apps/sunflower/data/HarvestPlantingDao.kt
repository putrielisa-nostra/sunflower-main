/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [HarvestPlant] class.
 */
@Dao
interface HarvestPlantingDao {
    @Query("SELECT * FROM harvest_plantings")
    fun getHarvestPlantings(): Flow<List<HarvestPlant>>

    @Query("SELECT EXISTS(SELECT 1 FROM harvest_plantings WHERE harvest_plant_id = :plantId LIMIT 1)")
    fun isHarvest(plantId: String): Flow<Boolean>

    /**
     * This query will tell Room to query both the [GardenPlanting] and [HarvestPlant] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM harvest_plantings A " +
            "LEFT JOIN garden_plantings B ON A.harvest_plant_id = B.plant_id " +
            "LEFT JOIN plants C ON A.harvest_plant_id = C.id")
    fun getGardenHarvest(): Flow<List<GardenAndHarvest>>

    @Insert
    suspend fun insertHarvestPlant(gardenPlanting: HarvestPlant): Long

    @Delete
    suspend fun deleteHarvestPlant(gardenPlanting: HarvestPlant)
}
