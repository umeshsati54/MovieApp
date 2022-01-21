package com.usati.movieapp.data.model

data class MovieReviews(
    val id: Int,
    val page: Int,
    val results: MutableList<ResultX>,
    val total_pages: Int,
    val total_results: Int
)