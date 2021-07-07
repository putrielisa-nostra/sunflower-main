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

package com.example.feature

import com.example.core.database.repository.CartRepository
import com.example.core.database.repository.FoodRepository
import com.example.feature.ui.viewmodel.FoodViewModel
import kotlinx.coroutines.runBlocking
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltAndroidTest
class FoodViewModelTest {
    @Inject
    lateinit var foodRepo: FoodRepository

    @Inject
    lateinit var cartRepo : CartRepository

    @Test
    fun testCheckSessionExpiry() = runBlocking {
        val mainViewModel = FoodViewModel(foodRepo, cartRepo)

        val totalExecutionTime = measureTimeMillis {
            val isSessionExpired = mainViewModel.checkSessionExpiry()
            assertTrue(isSessionExpired)
        }

        print("Total Execution Time: $totalExecutionTime")
    }
}