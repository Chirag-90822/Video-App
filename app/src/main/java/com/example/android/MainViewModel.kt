package com.example.android

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {
    var text = MutableLiveData<String>()

    fun setText(s: String) {
        text.value = s
    }
}