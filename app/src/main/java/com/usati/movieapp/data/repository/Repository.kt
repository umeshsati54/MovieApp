package com.usati.movieapp.data.repository

import com.usati.movieapp.data.api.RetrofitInstance

class Repository {
    suspend fun getMovies(pageNumber: Int) =
        RetrofitInstance.api.getMovies(pageNumber)

    suspend fun searchMovie(pageNumber: Int, searchQuery: String) =
        RetrofitInstance.api.searchMovies(pageNumber, searchQuery)

    fun getMovieCredits(movieId: Int) =
        RetrofitInstance.api.getMovieCredits(movieId)

    fun getSimilarMovie(movieId: Int) =
        RetrofitInstance.api.getSimilarMovie(movieId)

    fun getMovieReviews(movieId: Int) =
        RetrofitInstance.api.getMovieReviews(movieId)

}