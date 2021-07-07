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

import android.graphics.Color
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.core.database.entity.Cart
import com.example.core.database.entity.Food
import com.example.feature.R
import com.example.feature.databinding.ActivityFoodBinding
import com.example.feature.ui.adapter.FoodAdapter
import com.example.feature.ui.viewmodel.CartViewModel
import com.example.feature.ui.viewmodel.FoodViewModel
import com.google.android.material.snackbar.Snackbar
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.content_food.*
import java.io.IOException


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
        cartViewModel.cartList.observe(this, Observer {

            try {
                var countTotalItem = 0
                var totalPrice = 0
                it.let {
                    it.forEach {
                        countTotalItem += it.item_total
                        totalPrice += (it.item_total * it.item_price.toInt())
                    }
                }
                binding.cartCount.setText(countTotalItem.toString())
                //binding.totalPrice.setText(totalPrice.toString())

                val snackbar =
                    Snackbar.make(
                        this.coordinator_layout,
                        "Total Price:" + totalPrice.toString(),
                        Snackbar.LENGTH_SHORT
                    )
                snackbar.setBackgroundTint(Color.parseColor("#904a4646"))
                snackbar.setAnchorView(coordinator_layout)
                snackbar.show()
            } catch (ex: Exception) {
            }
        })
        binding.btnGoToCart.setOnClickListener {
            try {
                //val intent = Intent(this, CartActivity::class.java)
                //startActivity(intent)
                OneSignal.addTrigger("level", 11);
            } catch (ex: Exception) {
                throw RuntimeException("BtnGoToCartError: " + ex.toString())
            }
        }
    }


    fun removeCart(mealID: String, total: Int, price: String) {
        foodViewModel.RemoveItem(mealID, total, price)
    }

    fun updateCart(mealID: String, total: Int) {
        foodViewModel.UpdateItemCart(mealID, total)
    }

    fun createCart(mealID: String, total: Int, price: String) {
        foodViewModel.InsertCart(mealID, total, price)
    }

    fun isItemCart(mealID: String): Boolean {
        return foodViewModel.isItemCart(mealID)
    }

    fun getItemCart(mealID: String): Cart? {
        return foodViewModel.getItem(mealID)
    }

    fun interface Callback {
        fun add(food: Food?)
    }
}