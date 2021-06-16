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

package com.example.feature.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.database.repository.NotifRepository
import com.example.core.network.AuthResponse
import com.example.core.network.utilities.Resource
import com.example.core.network.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val mainRepo: NotifRepository) : ViewModel() {


    private val _sendNotification = SingleLiveEvent<Resource<AuthResponse>>()

    val sendNotification : LiveData<Resource<AuthResponse>> get() =  _sendNotification

    fun doSendNotification(name: String, token: String) =
        viewModelScope.launch {
            try {
                _sendNotification.value =  mainRepo.sendNotification(name, token)
            }
            catch (exception: Exception){

            }
        }


}