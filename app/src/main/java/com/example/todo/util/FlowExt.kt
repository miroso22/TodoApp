package com.example.todo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

private const val SHARING_TIMEOUT = 5_000L
fun <T> Flow<T>.stateIn(viewModel: ViewModel, initialValue: T) = stateIn(
    viewModel.viewModelScope,
    SharingStarted.WhileSubscribed(SHARING_TIMEOUT),
    initialValue = initialValue
)

fun <T> Flow<List<T>>.stateIn(viewModel: ViewModel, initialValue: List<T> = listOf()) = stateIn(
    viewModel.viewModelScope,
    SharingStarted.WhileSubscribed(SHARING_TIMEOUT),
    initialValue = initialValue
)