package com.usati.movieapp.data.model

data class Credits(
    val cast: MutableList<Cast>,
    val crew: MutableList<Crew>,
    val id: Int
)