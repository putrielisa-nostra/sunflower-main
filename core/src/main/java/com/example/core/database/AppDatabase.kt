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

package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.core.database.entity.Cart
import com.example.core.database.entity.GardenPlanting
import com.example.core.database.entity.HarvestPlant
import com.example.core.database.entity.Plant
import com.example.core.database.utilities.DATABASE_NAME
import com.example.core.database.utilities.SeedDatabaseWorker
import com.example.core.database.converter.Converters
import com.example.core.database.dao.*
import com.example.core.database.entity.Food

/**
 * The Room database for this app
 */
@Database(version = 4,
        entities = [GardenPlanting::class, Plant::class, HarvestPlant::class, Cart::class, Food::class],
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantingDao(): GardenPlantingDao
    abstract fun plantDao(): PlantDao
    abstract fun harvestDao(): HarvestPlantingDao
    abstract fun cartDao(): CartDao
    abstract fun foodDao(): FoodDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE garden_plantings "
                        + "ADD COLUMN last_plant_fertilize TEXT NOT NULL Default '2000-01-01'")
            }
        }
        private val MIGRATION_2_3: Migration = object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS meals (page INTEGER NOT NULL, idMeal TEXT NOT NULL, strMeal TEXT NOT NULL, strCategory TEXT NOT NULL, strMealThumb TEXT NOT NULL, PRIMARY KEY(idMeal))")
            }
        }
        private val MIGRATION_3_4: Migration = object :Migration(3,4){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE cart_plants")
                database.execSQL("CREATE TABLE IF NOT EXISTS item_cart (item_id TEXT NOT NULL, item_total INTEGER NOT NULL, PRIMARY KEY(item_id))")
            }
        }

    }
}

