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

package com.google.samples.apps.sunflower.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.databinding.ActivityCartBinding
import com.google.samples.apps.sunflower.databinding.ActivityGardenBinding
import com.google.samples.apps.sunflower.databinding.FragmentCartBinding
import com.google.samples.apps.sunflower.databinding.FragmentHarvestBinding
import com.google.samples.apps.sunflower.ui.adapters.CartAdapter
import com.google.samples.apps.sunflower.ui.adapters.HarvestAdapter
import com.google.samples.apps.sunflower.ui.viewmodels.CartViewModel
import com.google.samples.apps.sunflower.ui.viewmodels.GardenHarvestViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    //private val cartVM by viewModel<CartViewModel>()
    private val cartVM: CartViewModel by viewModels()

    private val binding : ActivityCartBinding by lazy {
        DataBindingUtil.setContentView<ActivityCartBinding>(this, R.layout.activity_cart).apply {
            lifecycleOwner = this@CartActivity
            viewModel = cartVM
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cartAdapter = CartAdapter()

        binding.adapter = cartAdapter

        // Observe data from viewModel
        cartVM.dataCarts.observe(this, Observer {
            Log.e("Cart-Activity", it.toString())
            it.let(cartAdapter::submitList)
        })
    }
}