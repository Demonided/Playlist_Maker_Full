package com.katoklizm.playlist_maker_full.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<STATE> : ViewModel() {

    val _state = MutableLiveData<STATE>()
    val state: LiveData<STATE>
        get() = _state

}