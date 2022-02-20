package com.brijwel.zohotask.ui.feeds

sealed class RefreshState {
    data class Loading(val isLoading: Boolean) : RefreshState()
    data class Error(val error: String) : RefreshState()
}