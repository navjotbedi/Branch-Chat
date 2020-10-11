package com.branch.chat.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.branch.chat.data.repository.MessageRepository
import com.branch.chat.network.STATUS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MessageViewModel @ViewModelInject constructor(
    private val messageRepository: MessageRepository
) : BaseViewModel() {

    init {
        _loadingStatus.postValue(STATUS.LOADING)
        fetchMessages()
    }

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                messageRepository.fetchMessages()
                    .flowOn(Dispatchers.IO)
                    .onCompletion {
                        _loadingStatus.postValue(STATUS.SUCCESS)
                    }.collect()
            } catch (ex: Exception) {
                _loadingStatus.postValue(STATUS.FAILED(ex.message))
            }
        }
    }

}