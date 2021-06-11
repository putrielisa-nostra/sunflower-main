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

package com.example.feature.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.feature.R
import com.example.feature.databinding.ActivityFoodBinding
import com.example.feature.ui.adapter.FoodAdapter
import com.example.feature.ui.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodActivity : AppCompatActivity() {
    private val cartVM: FoodViewModel by viewModels()

    private val binding : ActivityFoodBinding by lazy {
        DataBindingUtil.setContentView<ActivityFoodBinding>(this, R.layout.activity_food).apply {
            lifecycleOwner = this@FoodActivity
            vm = cartVM
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foodAdapter = FoodAdapter()
        binding.adapter = foodAdapter

        // Observe data from viewModel
        cartVM.foodList.observe(this, Observer {
            Log.e("Food-Activity", it.toString())
            it.let(foodAdapter::submitList)
        })
    }
}