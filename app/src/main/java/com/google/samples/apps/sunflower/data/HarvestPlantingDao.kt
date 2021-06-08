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
