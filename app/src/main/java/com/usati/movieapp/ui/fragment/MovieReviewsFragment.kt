package com.usati.movieapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.usati.movieapp.R
import com.usati.movieapp.data.model.MovieReviews
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.data.model.ResultX
import com.usati.movieapp.data.repository.Repository
import com.usati.movieapp.ui.MainActivity
import com.usati.movieapp.ui.adapter.MovieReviewsAdapter
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_reviews.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieReviewsFragment : Fragment(R.layout.fragment_reviews) {
    lateinit var viewModel: MoviesViewModel
    private lateinit var repository: Repository
    lateinit var movieReviewsAdapter: MovieReviewsAdapter
    private val args: MovieReviewsFragmentArgs by navArgs()
    lateinit var movie: Result

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        repository = (activity as MainActivity).repository
        movie = args.movie

        val castCall: Call<MovieReviews> = repository.getMovieReviews(movie.id)
        castCall.enqueue(object : Callback<MovieReviews?> {
            override fun onResponse(
                call: Call<MovieReviews?>?,
                response: Response<MovieReviews?>
            ) {
                hideProgressBar()
                val reviews: MutableList<ResultX>? = response.body()?.results

                movieReviewsAdapter = MovieReviewsAdapter(reviews!!)
                rvReviews.apply {
                    adapter = movieReviewsAdapter
                    layoutManager =
                        LinearLayoutManager(activity)
                }
            }

            override fun onFailure(call: Call<MovieReviews?>?, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    var isLoading = false
    private fun hideProgressBar() {
        reviewsProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        reviewsProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
}