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

package com.example.feature.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.database.entity.FoodAndCart
import com.example.feature.databinding.ContentCartBinding

class CartAdapter : ListAdapter<FoodAndCart, RecyclerView.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartViewHolder(
            ContentCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cart = getItem(position)
        (holder as CartViewHolder).bind(cart)
    }

    class CartViewHolder(
        private val binding: ContentCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
//            binding.setClickListener {
//                binding.plant?.let { plant ->
//                    navigateToPlant(plant, it)
//                }
//            }
        }

        fun bind(item: FoodAndCart) {
            binding.apply {
                cart = item
                executePendingBindings()
            }
        }
    }
}

private class CartDiffCallback : DiffUtil.ItemCallback<FoodAndCart>() {

    override fun areItemsTheSame(oldItem: FoodAndCart, newItem: FoodAndCart): Boolean {
        return oldItem.food.idMeal == newItem.food.idMeal
    }

    override fun areContentsTheSame(oldItem: FoodAndCart, newItem: FoodAndCart): Boolean {
        return oldItem.food.idMeal == newItem.food.idMeal
    }
}