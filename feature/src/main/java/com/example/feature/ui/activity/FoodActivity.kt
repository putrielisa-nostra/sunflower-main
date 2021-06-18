/*
 * Copyright 2021 Go ogle LLC
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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.core.database.entity.Cart
import com.example.core.database.entity.Food
import com.example.feature.R
import com.example.feature.databinding.ActivityFoodBinding
import com.example.feature.ui.adapter.FoodAdapter
import com.example.feature.ui.viewmodel.CartViewModel
import com.example.feature.ui.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_food.*


@AndroidEntryPoint
class FoodActivity : AppCompatActivity() {
    val foodViewModel: FoodViewModel by viewModels()
    val cartViewModel: CartViewModel by viewModels()

    private val binding: ActivityFoodBinding by lazy {
        DataBindingUtil.setContentView<ActivityFoodBinding>(this, R.layout.activity_food).apply {
            lifecycleOwner = this@FoodActivity
            vm = foodViewModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foodAdapter = FoodAdapter()
        binding.adapter = foodAdapter
        foodViewModel.foodList.observe(this, Observer {
            Log.e("Food-Activity", it.toString())
            it.let(foodAdapter::submitList)
        })
        cartViewModel.cartList.observe(this, Observer{
            var countTotalItem:Int=0
            it.let {
                it.forEach {
                    countTotalItem += it.item_total
                }
            }
            binding.cartCount.setText(countTotalItem.toString())
        })
        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
    fun RemoveCart(mealID: String, total: Int){
        foodViewModel.RemoveItem(mealID, total)
    }
    fun UpdateCart(mealID: String, total: Int){
        foodViewModel.UpdateItemCart(mealID,total)
    }
    fun CreateCart(mealID: String, total: Int){
        foodViewModel.InsertCart(mealID,total)
    }
    fun isItemCart(mealID: String):Boolean{
         return foodViewModel.isItemCart(mealID)
    }
    fun getItemCart(mealID: String): Cart? {
        return foodViewModel.getItem(mealID)
    }

    fun interface Callback {
        fun add(food: Food?)
    }
}