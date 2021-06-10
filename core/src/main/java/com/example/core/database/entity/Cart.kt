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

package com.example.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "cart_plants")
data class Cart(
        @ColumnInfo(name = "cart_plant_id") val cart_plant_id: String,
        @ColumnInfo(name = "item_total")val item_total: Int,
        @ColumnInfo(name = "item_price")val item_price: Float,
        @ColumnInfo(name = "cart_date") val cart_date: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_id")
    var cartID: Long = 0
}
