
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
data class FoodAndCart(
    @Embedded
    val food: Food,

    @Relation(parentColumn = "idMeal", entityColumn = "item_id")
    val cartItems: List<Cart> = emptyList()
)