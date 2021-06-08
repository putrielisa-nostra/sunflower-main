
package com.google.samples.apps.sunflower.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_plants")
    fun getAllCart(): Flow<List<Cart>>

    @Transaction
    @Query("SELECT * FROM cart_plants A " +
            "LEFT JOIN harvest_plantings B ON A.cart_plant_id = B.harvest_plant_id " +
            "LEFT JOIN plants C ON A.cart_plant_id = C.id")
    fun getharvestCart(): Flow<List<CartAndHarvest>>

    @Insert
    suspend fun insertItemCart(cartPlant: Cart): Long

    @Delete
    suspend fun removeItemCart(cartPlant: Cart)
}
