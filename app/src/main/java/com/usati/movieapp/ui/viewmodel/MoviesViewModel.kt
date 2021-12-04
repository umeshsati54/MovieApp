package com.usati.movieapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usati.movieapp.MoviesApplication
import com.usati.movieapp.data.model.MovieResponse
import com.usati.movieapp.data.repository.Repository
import com.usati.movieapp.data.model.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class MoviesViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    val movies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var pageNumber = 1
    var movieResponse: MovieResponse? = null

    val searchMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchPageNumber = 1
    var searchMovieResponse: MovieResponse? = null

    init {
        getMovies()
    }

    fun getMovies() = viewModelScope.launch {
        safeMovieCall()
    }

    fun searchMovies(searchQuery: String) = viewModelScope.launch {
        safeSearchMovieCall(searchQuery)
    }

    private fun handleMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                pageNumber++
                if (movieResponse == null) {
                    movieResponse = resultResponse
                } else {
                    val oldMovies = movieResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchPageNumber++
                if (searchMovieResponse == null) {
                    searchMovieResponse = resultResponse
                } else {
                    val oldArticles = searchMovieResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeMovieCall() {
        movies.postValue(Resource.Loading())
        try {
            if (isConnected()) {
                val response = repository.getMovies(pageNumber)
                movies.postValue(handleMovieResponse(response))
            } else {
                movies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> movies.postValue(Resource.Error("Network Failure"))
                else -> movies.postValue(Resource.Error(t.message.toString()))
            }
        }
    }

    private suspend fun safeSearchMovieCall(searchQuery: String) {
        searchMovies.postValue(Resource.Loading())
        try {
            if (isConnected()) {
                val response = repository.searchMovie(searchPageNumber, searchQuery)
                searchMovies.postValue(handleSearchMovieResponse(response))
            } else {
                searchMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchMovies.postValue(Resource.Error("Network Failure"))
                else -> searchMovies.postValue(Resource.Error(t.message.toString()))
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getApplication<MoviesApplication>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}