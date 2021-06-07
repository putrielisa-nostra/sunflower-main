/*
 * Copyright 2019 Google LLC
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

package com.google.samples.apps.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.samples.apps.sunflower.adapters.GARDEN_HARVEST_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.MY_GARDEN_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.SunflowerPagerAdapter
import com.google.samples.apps.sunflower.databinding.FragmentViewPagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> R.drawable.garden_tab_selector
            PLANT_LIST_PAGE_INDEX -> R.drawable.plant_list_tab_selector
            GARDEN_HARVEST_PAGE_INDEX -> R.drawable.harvest_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            GARDEN_HARVEST_PAGE_INDEX -> "Harvest"
            else -> null
        }
    }
//
//
//    private fun drawBadge(number: Int, gravity: Int) {
//        val drawableResId = when (radioGroup.checkedRadioButtonId) {
//            R.id.radioButtonSelectorDrawable -> R.drawable.selector_badge
//            R.id.radioButtonVectorDrawable   -> R.drawable.ic_notifications
//            else                             -> R.drawable.ic_launcher
//        }
//
//        DrawableBadge.Builder(applicationContext)
//            .drawableResId(drawableResId)
//            .badgeColor(R.color.badgeColor)
//            .badgeSize(R.dimen.badge_size)
//            .badgeGravity(gravity)
//            .textColor(R.color.textColor)
//            .showBorder(true)
//            .badgeBorderColor(R.color.borderColor)
//            .badgeBorderSize(R.dimen.badge_border_size)
//            .maximumCounter(99)
//            .badgeMargin(10f)
//            .showCounter(true)
//            .build()
//            .get(number)
//            .let { drawable -> imageViewBadge.setImageDrawable(drawable) }
//    }
}
