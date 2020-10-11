package com.branch.chat.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.branch.chat.data.api.MessageRequest
import com.branch.chat.data.repository.MessageRepository
import com.branch.chat.network.STATUS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class ConversationViewModel @ViewModelInject constructor(
    private val messageRepository: MessageRepository
) : BaseViewModel() {

    val message = MutableLiveData<String>()

    fun sendMessage(threadId: Long) {
        message.value?.let {
            viewModelScope.launch {
                try {
                    _loadingStatus.postValue(STATUS.LOADING)
                    flowOf(messageRepository.sendMessage(MessageRequest(threadId, it)))
                        .map { response ->
                            messageRepository.storeAllMessages(listOf(response))
                        }
                        .flowOn(Dispatchers.IO)
                        .onCompletion {
                            _loadingStatus.postValue(STATUS.SUCCESS)
                        }.collect()
                }catch (ex: Exception){
                    _loadingStatus.postValue(STATUS.FAILED(ex.message))
                }
            }
        }
    }

}