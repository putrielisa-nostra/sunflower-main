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

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "meals")
@Parcelize
@JsonClass(generateAdapter = true)
data class Food(
    var page: Int = 0,
    @field:Json(name = "idMeal") @PrimaryKey val idMeal: String,
    @field:Json(name = "strMeal") val strMeal: String,
    @field:Json(name = "strCategory") val strCategory: String,
    @field:Json(name = "strMealThumb") val strMealThumb: String,
    @field:Json(name = "price") val price: String = "500"
) : Parcelable {
}
