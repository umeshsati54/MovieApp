package com.usati.movieapp.data.model

data class MovieResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)