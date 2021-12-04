package com.usati.movieapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.usati.movieapp.data.repository.Repository

class MoviesViewModelProviderFactory(
    val app: Application,
    private val repo: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(app, repo) as T
    }
}