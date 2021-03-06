package com.usati.movieapp.data.api

import com.usati.movieapp.data.model.*
import com.usati.movieapp.utils.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("3/discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>

    //https://api.themoviedb.org/3/search/movie?page=2&query=batman&api_key=2ba9bca648dc0bcba720d09021b80304
    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("page") page: Int = 1,
        @Query("query") searchQuery: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("3/discover/movie")
    fun getMoviesFlipper(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieResponse>

    //https://api.themoviedb.org/3/movie/45162/credits?api_key=2ba9bca648dc0bcba720d09021b80304
    @GET("3/movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<Credits>

    //https://api.themoviedb.org/3/movie/471474/similar?page=2&api_key=2ba9bca648dc0bcba720d09021b80304
    @GET("3/movie/{movie_id}/similar")
    fun getSimilarMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieResponse>

    //https://api.themoviedb.org/3/movie/471474/reviews?page=1&api_key=2ba9bca648dc0bcba720d09021b80304
    @GET("3/movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieReviews>
}