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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.database.entity.Food
import com.example.feature.databinding.ContentFoodBinding
import com.example.feature.ui.activity.FoodActivity
import kotlinx.android.synthetic.main.activity_food.*
import java.lang.Exception


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

    class FoodViewHolder(
        private val binding: ContentFoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            var mContext: Context = super.itemView.context
            binding.btnDelete.setOnClickListener() { view ->
                fun crtUser(view: View) {
                    var total: Int = (binding.edittotal.text.toString()).toInt()
                    binding.btnAdd.isGone = false
                    binding.linear.isGone = true
                    binding.btnDelete.isGone = true
                    (mContext as FoodActivity).RemoveCart(binding.food?.idMeal.toString(),total)
                    binding.edittotal.setText("0")
                }
                crtUser(view)
            }
            binding.btnAdd.setOnClickListener { view ->
                fun crtUser(view: View) {
                    binding.btnAdd.isGone = true
                    binding.linear.isGone = false
                    binding.btnDelete.isGone = false
                    binding.edittotal.setText("1")
                    (mContext as FoodActivity).CreateCart(binding.food?.idMeal.toString(), 1)
                }
                crtUser(view)
            }
            binding.btnPlus.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (binding.edittotal.text.toString()).toInt() + 1
                    binding.edittotal.setText(total.toString())
                    if(total>0){
                        (mContext as FoodActivity).UpdateCart(binding.food?.idMeal.toString(), total)
                    }
                }
                crtUser(view)
            }
            binding.btnMin.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (binding.edittotal.text.toString()).toInt()
                    if (total > 0) {
                        total = total - 1
                        binding.edittotal.setText(total.toString())
                        (mContext as FoodActivity).UpdateCart(
                            binding.food?.idMeal.toString(),
                            total
                        )
                    }
                    if(total==0){
                        (mContext as FoodActivity).RemoveCart(binding.food?.idMeal.toString(), total)
                    }
                }
                crtUser(view)
            }
        }

        fun bind(item: Food) {
            binding.apply {
                food = item
                Thread {
                    var mContext: Context = super.itemView.context
                    val isCart =
                        (mContext as FoodActivity).isItemCart(binding.food?.idMeal.toString())
                    val cart =
                        (mContext as FoodActivity).getItemCart(binding.food?.idMeal.toString())
                    if (cart != null) {
                        binding.edittotal.setText(cart.item_total.toString())
                    }
                    binding.btnAdd.isGone = isCart
                    binding.linear.isGone = !isCart
                    binding.btnDelete.isGone = !isCart
                }.start()

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

