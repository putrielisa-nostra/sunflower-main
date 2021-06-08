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

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.HarvestPlant
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.FragmentHarvestBinding
import com.google.samples.apps.sunflower.databinding.FragmentPlantDetailBinding
import com.google.samples.apps.sunflower.ui.adapters.GARDEN_HARVEST_PAGE_INDEX
import com.google.samples.apps.sunflower.ui.fragment.PlantDetailFragment.Callback
import com.google.samples.apps.sunflower.ui.viewmodels.PlantDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A fragment representing a single Plant detail screen.
 */
@AndroidEntryPoint
class PlantDetailFragment : Fragment() {

    private val plantDetailViewModel: PlantDetailViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentPlantDetailBinding>(
                inflater,
                R.layout.fragment_plant_detail,
                container,
                false
        ).apply {
            viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            callback = Callback { plant ->
                plant?.let {
                    hideAppBarFab(fab)
                    plantDetailViewModel.addPlantToGarden()
                    Snackbar.make(root, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
            btnHarvest.setOnClickListener { view ->
                fun crtUser(view: View) {
                    plantDetailViewModel.addPlantToHarvest((edittotal.text.toString()).toInt()
                    ).apply {
                        edittotal.setText("0")
                        Snackbar.make(root, R.string.added_plant_to_harvest, Snackbar.LENGTH_LONG)
                                .show()
                    }
                }
                crtUser(view)
                view.findNavController().navigateUp()
            }
            btnFert.setOnClickListener { view ->
                fun crtUser(view: View) {
                    plantDetailViewModel.UpdateLastFertilized()
                    val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.nav_default_pop_enter_anim)
                    btnFert.startAnimation(anim)
                    plantDateFert.setTextColor(Color.BLACK)
                    Snackbar.make(root, "Update last fertilized...", Snackbar.LENGTH_LONG)
                            .show()

                }
                crtUser(view)
            }
            btnPlus.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (edittotal.text.toString()).toInt()
                    total = total + 1
                    edittotal.setText(total.toString())
                }
                crtUser(view)
            }
            btnMin.setOnClickListener { view ->
                fun crtUser(view: View) {
                    var total: Int = (edittotal.text.toString()).toInt()
                    if (total > 0) {
                        total = total - 1
                        edittotal.setText(total.toString())
                    }
                }
                crtUser(view)
            }

            galleryNav.setOnClickListener { navigateToGallery() }

            var isToolbarShown = false

            // scroll change listener begins at Y = 0 when image is fully collapsed
            plantDetailScrollview.setOnScrollChangeListener(
                    NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                        // User scrolled past image to height of toolbar and the title text is
                        // underneath the toolbar, so the toolbar should be shown.
                        val shouldShowToolbar = scrollY > toolbar.height

                        // The new state of the toolbar differs from the previous state; update
                        // appbar and toolbar attributes.
                        if (isToolbarShown != shouldShowToolbar) {
                            isToolbarShown = shouldShowToolbar

                            // Use shadow animator to add elevation if toolbar is shown
                            appbar.isActivated = shouldShowToolbar

                            // Show the plant name if toolbar is shown
                            toolbarLayout.isTitleEnabled = shouldShowToolbar
                        }
                    }
            )

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun navigateToGallery() {
        plantDetailViewModel.plant.value?.let { plant ->
            val direction =
                    PlantDetailFragmentDirections.actionPlantDetailFragmentToGalleryFragment(plant.name)
            findNavController().navigate(direction)
        }
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareText = plantDetailViewModel.plant.value.let { plant ->
            if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
                .setText(shareText)
                .setType("text/plain")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    // FloatingActionButtons anchored to AppBarLayouts have their visibility controlled by the scroll position.
    // We want to turn this behavior off to hide the FAB when it is clicked.
    //
    // This is adapted from Chris Banes' Stack Overflow answer: https://stackoverflow.com/a/41442923
    private fun hideAppBarFab(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior
        behavior.isAutoHideEnabled = false
        fab.hide()
    }

    fun interface Callback {
        fun add(plant: Plant?)
    }
    fun interface Harvest{
        fun add(harvest: HarvestPlant?)
    }
}