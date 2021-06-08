/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.ui.viewmodels

import androidx.lifecycle.*
import com.google.samples.apps.sunflower.BuildConfig
import com.google.samples.apps.sunflower.ui.fragment.PlantDetailFragment
import com.google.samples.apps.sunflower.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * The ViewModel used in [PlantDetailFragment].
 */
@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    plantRepository: PlantRepository,
    val gardenPlantingRepository: GardenPlantingRepository,
    val harvestPlantRepository: HarvestPlantRepository
) : ViewModel() {
    val plantId: String = savedStateHandle.get<String>(PLANT_ID_SAVED_STATE_KEY)!!
    val isPlanted = gardenPlantingRepository.isPlanted(plantId).asLiveData()
    val plant = plantRepository.getPlant(plantId).asLiveData()
    val garden = gardenPlantingRepository.getGardenPlant(plantId).asLiveData()

    fun addPlantToGarden() {
        viewModelScope.launch {
            gardenPlantingRepository.createGardenPlanting(plantId)
        }
    }

    fun addPlantToHarvest(total:Int){
        viewModelScope.launch {
            harvestPlantRepository.createHarvestPlanting(plantId, total)
        }
    }

    fun UpdateLastFertilized(){
        viewModelScope.launch {
            Thread {
                val last_fert: String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                gardenPlantingRepository.UpdateGardenPlant(plantId, last_fert)
            }.start()

        }
    }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }
}
