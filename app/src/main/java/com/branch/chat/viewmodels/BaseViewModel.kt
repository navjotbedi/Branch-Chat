package com.branch.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.branch.chat.network.STATUS

open class BaseViewModel : ViewModel() {
    val loadingStatus: LiveData<STATUS>
        get() = _loadingStatus
    protected val _loadingStatus by lazy { MutableLiveData<STATUS>() }
}