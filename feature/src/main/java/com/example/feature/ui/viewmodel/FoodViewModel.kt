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

package com.example.feature.ui.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.example.core.database.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    var isLoading: Boolean = false
    lateinit var toastMessage: String

    private val foodFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    private val foodListFlow = foodFetchingIndex.flatMapLatest { page ->
        foodRepository.fetchFoodList(
            page = page,
            onStart = { isLoading = true },
            onComplete = { isLoading = false },
            onError = {
                if (it != null) {
                    toastMessage = it
                }
            }
        )
    }
    val foodList = foodListFlow.asLiveData()
    //val foodList: List<Food> by foodListFlow.flatMapLatest {  } (viewModelScope, emptyList())

    init {
        Timber.d("init FoodViewModel")
    }

    @MainThread
    fun fetchNextFoodList() {
        if (!isLoading) {
            foodFetchingIndex.value++
        }
    }
}
