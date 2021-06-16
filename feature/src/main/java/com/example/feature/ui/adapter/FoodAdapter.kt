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
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.database.entity.Food
import com.example.feature.databinding.ContentFoodBinding
import kotlinx.android.synthetic.main.content_food.*

class FoodAdapter : ListAdapter<Food, RecyclerView.ViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FoodViewHolder(
            ContentFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val food = getItem(position)
        (holder as FoodViewHolder).bind(food)
    }

    class FoodViewHolder(private val binding: ContentFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnDelete.setOnClickListener() { view ->
                fun crtUser(view: View) {
                    var total: Int = 0
                    binding.edittotal.setText(total.toString())
                    binding.btnAdd.visibility = View.VISIBLE
                    binding.linear.visibility = View.INVISIBLE
                    binding.btnDelete.visibility = View.INVISIBLE
                    binding.btnAdd.isGone = false
                    binding.linear.isGone = true
                }
                crtUser(view)
            }
            binding.btnAdd.setOnClickListener { view ->
                fun crtUser(view: View) {
                    binding.btnAdd.visibility = View.INVISIBLE
                    binding.linear.visibility = View.VISIBLE
                    binding.btnDelete.visibility = View.VISIBLE
                    binding.btnAdd.isGone = true
                    binding.linear.isGone = false
                }
                crtUser(view)
            }

            binding.btnPlus.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (binding.edittotal.text.toString()).toInt()
                    total = total + 1
                    binding.edittotal.setText(total.toString())
                }
                crtUser(view)
            }
            binding.btnMin.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (binding.edittotal.text.toString()).toInt()
                    if (total > 0) {
                        total = total - 1
                        binding.edittotal.setText(total.toString())
                    }
                }
                crtUser(view)
            }
        }

        fun bind(item: Food) {
            binding.apply {
                food = item
                executePendingBindings()
            }
        }
    }
}

private class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {

    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem.idMeal == newItem.idMeal
    }

    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }
}

//
//private class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {
//
//    override fun areItemsTheSame(
//        oldItem: Food,
//        newItem: Food
//    ): Boolean {
//        return oldItem.idMeal == newItem.idMeal
//    }
//
//    override fun areContentsTheSame(
//        oldItem: Food,
//        newItem: Food
//    ): Boolean {
//        return oldItem.idMeal == newItem.idMeal
//    }
//}


//
//class FoodAdapter : BindingListAdapter<Food, FoodAdapter.FoodViewHolder>(diffUtil) {
//
//    private var onClickedAt = 0L
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
//        val binding = parent.binding<ContentFoodBinding>(R.layout.content_food)
//        return FoodViewHolder(binding).apply {
//            binding.root.setOnClickListener {
//                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
//                    ?: return@setOnClickListener
//                val currentClickedAt = SystemClock.elapsedRealtime()
//                if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
//                    //DetailActivity.startActivity(binding.transformationLayout, getItem(position))
//                    onClickedAt = currentClickedAt
//                }
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
//        holder.binding.apply {
//            food = getItem(position)
//            executePendingBindings()
//        }
//    }
//
//    class FoodViewHolder(val binding: ContentFoodBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    companion object {
//        private val diffUtil = object : DiffUtil.ItemCallback<Food>() {
//
//            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean =
//                oldItem.strMeal == newItem.strMeal
//
//            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean =
//                oldItem == newItem
//        }
//    }
//}
