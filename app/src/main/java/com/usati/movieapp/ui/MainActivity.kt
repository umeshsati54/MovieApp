package com.usati.movieapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.usati.movieapp.R
import com.usati.movieapp.data.repository.Repository
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import com.usati.movieapp.ui.viewmodel.MoviesViewModelProviderFactory


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MoviesViewModel
    lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = Repository()
        val viewModelProviderFactory = MoviesViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MoviesViewModel::class.java]
    }
}