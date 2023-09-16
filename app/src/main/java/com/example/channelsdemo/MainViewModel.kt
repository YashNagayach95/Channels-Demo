package com.example.channelsdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    sealed class MyEvents {
        data class ErrorEvent(val message: String) : MyEvents()
    }

    private val eventsChannel = Channel<MyEvents>()
    val eventsFlows = eventsChannel.receiveAsFlow()

    fun triggerEvent() = viewModelScope.launch {
        eventsChannel.send(MyEvents.ErrorEvent("This is the error"))
    }
}