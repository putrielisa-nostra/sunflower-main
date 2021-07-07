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
import com.example.core.database.converter.Converters
import com.example.core.database.dao.*
import com.example.core.database.entity.*
import com.example.core.database.utilities.DATABASE_NAME
import com.example.core.database.utilities.SeedDatabaseWorker
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import net.sqlcipher.database.SupportFactory

/**
 * The Room database for this app
 */
@Database(
    version = 7,
    entities = [GardenPlanting::class, Plant::class, HarvestPlant::class, Cart::class, Food::class],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantingDao(): GardenPlantingDao
    abstract fun plantDao(): PlantDao
    abstract fun harvestDao(): HarvestPlantingDao
    abstract fun cartDao(): CartDao
    abstract fun foodDao(): FoodDao

    //    companion object {
//        fun getInstance(context: Context):
//                AppDatabase = buildDatabase(context)
//
//        private fun buildDatabase(
//            context: Context
//        ): AppDatabase {
//            // DatabaseKeyMgr is a singleton that all of the above code is wrapped into.
//            // Ideally this should be injected through DI but to simplify the sample code
//            // we'll retrieve it as follows
//            //val dbKey = WorkManager.getInstance().getCharKey(passcode, context)
//            val supportFactory = SupportFactory(SQLiteDatabase.getBytes("thissunflower".toCharArray()))
//            return Room.databaseBuilder(context, AppDatabase::class.java,
//                DATABASE_NAME).openHelperFactory(supportFactory).build()
//        }
//    }
    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {

            val passphrase: ByteArray =
                SQLiteDatabase.getBytes("thissunflower".toCharArray())

            val factory = SupportFactory(passphrase, object : SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase?) {
                }

                override fun postKey(database: SQLiteDatabase?) {
                    database?.execSQL("PRAGMA kdf_iter = 64000;")
                    database?.execSQL("PRAGMA cipher_page_size = 1024;")
                    database?.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;")
                    database?.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;")
                }
            }, false)

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
                .openHelperFactory(factory)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .addMigrations(MIGRATION_4_5)
                .addMigrations(MIGRATION_5_6)
                .addMigrations(MIGRATION_6_7)
                .build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE garden_plantings "
                            + "ADD COLUMN last_plant_fertilize TEXT NOT NULL Default '2000-01-01'"
                )
            }
        }
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS meals (page INTEGER NOT NULL, idMeal TEXT NOT NULL, strMeal TEXT NOT NULL, strCategory TEXT NOT NULL, strMealThumb TEXT NOT NULL, PRIMARY KEY(idMeal))")
            }
        }
        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE cart_plants")
                database.execSQL("CREATE TABLE IF NOT EXISTS item_cart (item_id TEXT NOT NULL, item_total INTEGER NOT NULL, PRIMARY KEY(item_id))")
            }
        }
        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE meals "
                            + "ADD COLUMN price TEXT NOT NULL Default '0'"
                )
            }
        }
        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS meals_temp (page INTEGER NOT NULL, idMeal TEXT NOT NULL, strMeal TEXT NOT NULL, strCategory TEXT NOT NULL, strMealThumb TEXT NOT NULL, price TEXT NOT NULL DEFAULT '500', PRIMARY KEY(idMeal))")
                database.execSQL("INSERT INTO meals_temp (page, idMeal, strMeal, strCategory, strMealThumb, price) SELECT page, idMeal, strMeal, strCategory, strMealThumb, price FROM meals")
                database.execSQL("DROP TABLE meals")
                database.execSQL("ALTER TABLE meals_temp RENAME TO meals")
            }
        }
        private val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE item_cart "
                            + "ADD COLUMN item_price TEXT NOT NULL Default '0'"
                )
            }
        }
    }
}

