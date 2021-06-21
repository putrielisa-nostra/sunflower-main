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
import com.example.core.database.entity.GardenAndHarvest
import com.example.core.database.entity.HarvestPlant
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [HarvestPlant] class.
 */
@Dao
interface HarvestPlantingDao {
    @Query("SELECT * FROM harvest_plantings")
    fun getHarvestPlantings(): Flow<List<HarvestPlant>>

    @Query("SELECT EXISTS(SELECT 1 FROM harvest_plantings WHERE harvest_plant_id = :plantId LIMIT 1)")
    fun isHarvest(plantId: String): Boolean

    @Transaction
    @Query("SELECT * FROM harvest_plantings A " +
            "LEFT JOIN garden_plantings B ON A.harvest_plant_id = B.plant_id " +
            "LEFT JOIN plants C ON A.harvest_plant_id = C.id " +
            "GROUP BY A.harvest_plant_id")
    fun getOneListHarvest(): Flow<List<GardenAndHarvest>>

    @Transaction
    @Query("SELECT * FROM harvest_plantings A " +
            "LEFT JOIN garden_plantings B ON A.harvest_plant_id = B.plant_id " +
            "LEFT JOIN plants C ON A.harvest_plant_id = C.id " +
            "WHERE A.harvest_plant_id = :plantId")
    fun getHarvestByPlant(plantId: String): List<HarvestPlant>

    @Query("UPDATE harvest_plantings SET harvest_amount = :total WHERE harvest_plant_id = :plantID")
    suspend fun updateHarvest(plantID: String, total: Int)

    @Insert
    suspend fun insertHarvestPlant(gardenPlanting: HarvestPlant): Long

    @Delete
    suspend fun deleteHarvestPlant(gardenPlanting: HarvestPlant)
}
