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

package com.google.samples.apps.sunflower.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.core.database.entity.HarvestPlant
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.ui.adapters.HarvestAdapter
import com.google.samples.apps.sunflower.ui.adapters.MY_GARDEN_PAGE_INDEX
import com.google.samples.apps.sunflower.databinding.FragmentHarvestBinding
import com.google.samples.apps.sunflower.ui.viewmodels.GardenHarvestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HarvestFragment : Fragment(){

    private lateinit var binding: FragmentHarvestBinding

    private val viewModel: GardenHarvestViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvestBinding.inflate(inflater, container, false)
        val adapter = HarvestAdapter()
        binding.harvestList.adapter = adapter
        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }
        subscribeUi(adapter, binding)
        return binding.root
    }
    fun getCountPlant(plantID: String):String{
         val data = viewModel.getHarvestByPlant(plantID)
        var count=0
        data.forEach {
            count += it.harvest_amount
        }
        return count.toString()
    }
    private fun subscribeUi(adapter: HarvestAdapter, binding: FragmentHarvestBinding) {
        viewModel.getOneListHarvest.observe(viewLifecycleOwner) { result ->
            binding.hasPlantings = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

     //TODO: convert to data binding if applicable
    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
                MY_GARDEN_PAGE_INDEX
    }
}
