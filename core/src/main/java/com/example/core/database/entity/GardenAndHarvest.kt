
package com.example.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.core.database.entity.GardenPlanting
import com.example.core.database.entity.HarvestPlant
import com.example.core.database.entity.Plant

/**
 * This class captures the relationship between a [Plant] and a user's [GardenPlanting], which is
 * used by Room to fetch the related entities.
 */
data class GardenAndHarvest(
    @Embedded
    val plant: Plant,

    @Relation(parentColumn = "id", entityColumn = "harvest_plant_id")
    val harvestPlantings: List<HarvestPlant> = emptyList(),

    @Embedded
    val garden: GardenPlanting,

    @Relation(parentColumn = "plant_id", entityColumn = "harvest_plant_id")
    val harvestGarden: List<HarvestPlant> = emptyList()
)