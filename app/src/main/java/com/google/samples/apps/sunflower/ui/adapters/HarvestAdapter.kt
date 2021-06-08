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

package com.google.samples.apps.sunflower.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.GardenAndHarvest
import com.google.samples.apps.sunflower.databinding.ListItemHarvestPlantingBinding
import com.google.samples.apps.sunflower.ui.fragment.HomeViewPagerFragmentDirections
import com.google.samples.apps.sunflower.ui.viewmodels.GardenAndHarvestViewModel

class HarvestAdapter :
    ListAdapter<GardenAndHarvest, HarvestAdapter.ViewHolder>(
        HarvestPlantDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_harvest_planting,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ListItemHarvestPlantingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.viewModel?.plantId?.let { plantId ->
                    navigateToPlant(plantId, view)
                }
            }
        }

        private fun navigateToPlant(plantId: String, view: View) {
            val direction = HomeViewPagerFragmentDirections
                .actionViewPagerFragmentToPlantDetailFragment(plantId)
            view.findNavController().navigate(direction)
        }

        fun bind(harvests: GardenAndHarvest) {
            with(binding) {
                viewModel = GardenAndHarvestViewModel(harvests)
                executePendingBindings()
            }
        }
    }
}

private class HarvestPlantDiffCallback : DiffUtil.ItemCallback<GardenAndHarvest>() {

    override fun areItemsTheSame(
        oldItem: GardenAndHarvest,
        newItem: GardenAndHarvest
    ): Boolean {
        return oldItem.garden.plantId == newItem.garden.plantId
    }

    override fun areContentsTheSame(
        oldItem: GardenAndHarvest,
        newItem: GardenAndHarvest
    ): Boolean {
        return oldItem.garden == newItem.garden
    }
}
