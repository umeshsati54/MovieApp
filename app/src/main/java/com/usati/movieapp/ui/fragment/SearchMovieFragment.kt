package com.usati.movieapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usati.movieapp.utils.Constants
import com.usati.movieapp.utils.Constants.Companion.SEARCH_TIME_DELAY
import com.usati.movieapp.ui.MainActivity
import com.usati.movieapp.R
import com.usati.movieapp.ui.adapter.MoviesAdapter
import com.usati.movieapp.data.model.Resource
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchMovieFragment : Fragment(R.layout.fragment_search_movie) {
    lateinit var viewModel: MoviesViewModel
    lateinit var moviesAdapter: MoviesAdapter
    private val TAG = "SearchMovieFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_searchMovieFragment_to_movieDetailsFragment,
                bundle
            )
        }

        var job: Job? = null

        etSearch.editText?.doOnTextChanged { text, start, before, count ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                text?.let {
                    if (text.toString().isNotEmpty()) {
                        viewModel.searchMovieResponse = null
                        viewModel.searchPageNumber = 1
                        viewModel.searchMovies(text.toString())
                    }
                }
            }
        }

        viewModel.searchMovies.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    tvErrorSearch.text = ""
                    response.data?.let { movieResponse ->
                        moviesAdapter.differ.submitList(movieResponse.results.toList())
                        val totalPages = movieResponse.total_pages// / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchPageNumber == totalPages
                        if (isLastPage) {
                            rvSearchMovie.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        tvErrorSearch.text = message
                    }
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchMovies(etSearch.editText?.text.toString())
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter()
        rvSearchMovie.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(activity, 3)
            addOnScrollListener(this@SearchMovieFragment.scrollListener)
        }
    }
}