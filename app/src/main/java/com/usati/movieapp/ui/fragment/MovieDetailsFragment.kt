package com.usati.movieapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.usati.movieapp.R
import com.usati.movieapp.data.model.Cast
import com.usati.movieapp.data.model.Credits
import com.usati.movieapp.data.model.MovieResponse
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.data.repository.Repository
import com.usati.movieapp.ui.MainActivity
import com.usati.movieapp.ui.adapter.MovieCastAdapter
import com.usati.movieapp.ui.adapter.SimilarMoviesAdapter
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import kotlinx.android.synthetic.main.fragment_movie_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    lateinit var viewModel: MoviesViewModel
    lateinit var repository: Repository
    private val args: MovieDetailsFragmentArgs by navArgs()
    lateinit var similarMoviesAdapter: SimilarMoviesAdapter
    lateinit var castAdapter: MovieCastAdapter
    lateinit var movie: Result

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        repository = (activity as MainActivity).repository
        movie = args.movie

        Glide.with(this).load(IMAGE_URL + movie.poster_path).into(ivPoster)
        tvMovieName.text = movie.title
        tvRating.text = "${movie.vote_average}/10"
        tvPopularity.text = "${movie.popularity}"
        tvReleaseDate.text = movie.release_date
        tvDescription.text = movie.overview

        btnReviews.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", movie)
            }
            findNavController().navigate(
                R.id.action_movieDetailsFragment_to_movieReviewsFragment,
                bundle
            )
        }

        val castCall: Call<Credits> = repository.getMovieCredits(movie.id)
        val similarMoviesCall: Call<MovieResponse> = repository.getSimilarMovie(movie.id)
        //making the call
        castCall.enqueue(object : Callback<Credits?> {
            override fun onResponse(
                call: Call<Credits?>?,
                response: Response<Credits?>
            ) {

                val cast: MutableList<Cast>? = response.body()?.cast

                castAdapter = MovieCastAdapter(cast!!)
                rvCast.apply {
                    adapter = castAdapter
                    layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<Credits?>?, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })


        similarMoviesCall.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(
                call: Call<MovieResponse?>?,
                response: Response<MovieResponse?>
            ) {

                val movies: MutableList<Result>? = response.body()?.results

                similarMoviesAdapter = SimilarMoviesAdapter(movies!!)
                rvSimilarMovies.apply {
                    adapter = similarMoviesAdapter
                    layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                }

                similarMoviesAdapter.setOnItemClickListener {
                    val bundle = Bundle().apply {
                        putSerializable("movie", it)
                    }
                    findNavController().navigate(
                        R.id.action_movieDetailsFragment_self,
                        bundle
                    )
                }
            }

            override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}