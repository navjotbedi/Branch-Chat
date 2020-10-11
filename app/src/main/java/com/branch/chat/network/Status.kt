package com.branch.chat.network

sealed class STATUS {
    object LOADING : STATUS()
    object SUCCESS : STATUS()
    class FAILED(val message: String?) : STATUS()
}