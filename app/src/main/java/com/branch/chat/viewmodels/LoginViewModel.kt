package com.branch.chat.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.branch.chat.data.api.LoginModel
import com.branch.chat.network.ApiService
import com.branch.chat.network.STATUS
import com.branch.chat.utils.PreferenceManager
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginModel: LiveData<LoginModel>
        get() = _loginModelLiveData
    private val _loginModelLiveData by lazy { MutableLiveData<LoginModel>() }

    fun login(loginModel: LoginModel) {
        viewModelScope.launch {
            try {
                _loadingStatus.postValue(STATUS.LOADING)
                val response = apiService.login(loginModel)
                preferenceManager.authToken = response.authToken
                _loadingStatus.postValue(STATUS.SUCCESS)
            } catch (ex: Exception) {
                _loadingStatus.postValue(STATUS.FAILED(ex.message))
            }
        }
    }

    fun onClick() {
        _loginModelLiveData.postValue(LoginModel(email.value, password.value))
    }

}