package com.usati.movieapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import com.usati.movieapp.ui.MainActivity
import com.usati.movieapp.R
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    lateinit var viewModel: MoviesViewModel
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        val movie = args.movie

        Glide.with(this).load(IMAGE_URL + movie.poster_path).into(ivPoster)
        tvMovieName.text = movie.title
        tvRating.text = "${movie.vote_average}/10"
        tvPopularity.text = "${movie.popularity}"
        tvReleaseDate.text = movie.release_date
        tvDescription.text = movie.overview
    }
}