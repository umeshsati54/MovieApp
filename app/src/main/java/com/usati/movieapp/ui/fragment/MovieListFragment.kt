package com.usati.movieapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.usati.movieapp.R
import com.usati.movieapp.data.api.RetrofitInstance
import com.usati.movieapp.data.model.MovieResponse
import com.usati.movieapp.data.model.Resource
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.ui.MainActivity
import com.usati.movieapp.ui.adapter.FlipperAdapter
import com.usati.movieapp.ui.adapter.MoviesAdapter
import com.usati.movieapp.ui.adapter.SliderAdapter
import com.usati.movieapp.ui.viewmodel.MoviesViewModel
import com.usati.movieapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    lateinit var viewModel: MoviesViewModel
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var adapter: FlipperAdapter
    lateinit var sliderView: SliderView
    lateinit var sliderAdapter: SliderAdapter

    private val TAG = "MovieListFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val service = RetrofitInstance.api
        val call: Call<MovieResponse> = service.getMoviesFlipper()
        //making the call
        call.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(
                call: Call<MovieResponse?>?,
                response: Response<MovieResponse?>
            ) {

                val movies: MutableList<Result>? = response.body()?.results

                sliderView = activity?.findViewById(R.id.image_slider)!!
                sliderAdapter = SliderAdapter(movies!!)
                sliderView.setSliderAdapter(sliderAdapter)
                //sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
                sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
                sliderView.startAutoCycle()
                sliderAdapter.setOnItemClickListener {
                    val bundle = Bundle().apply {
                        putSerializable("movie", it)
                    }
                    findNavController().navigate(
                        R.id.action_movieListFragment_to_movieDetailsFragment,
                        bundle
                    )
                }

                //creating adapter object
                adapter = FlipperAdapter(context!!, movies!!)

//                adapterViewFlipper.adapter = adapter
//                adapterViewFlipper.flipInterval = 2000
//                adapterViewFlipper.startFlipping()
//                adapter.setOnItemClickListener {
//                    val bundle = Bundle().apply {
//                        putSerializable("movie", it)
//                    }
//                    findNavController().navigate(
//                        R.id.action_movieListFragment_to_movieDetailsFragment,
//                        bundle
//                    )
//                }
            }

            override fun onFailure(call: Call<MovieResponse?>?, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })


        setupRecyclerView()

        moviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_movieListFragment_to_movieDetailsFragment,
                bundle
            )
        }



        viewModel.movies.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    tvErrorList.text = ""
                    response.data?.let { movieResponse ->
                        moviesAdapter.differ.submitList(movieResponse.results.toList())
                        val totalPages = movieResponse.total_pages// / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.pageNumber == totalPages
                        if (isLastPage) {
                            rvMoviesList.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        tvErrorList.text = message
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
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getMovies()
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter()
        rvMoviesList.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(activity, 3)
            addOnScrollListener(this@MovieListFragment.scrollListener)
        }
    }

//    private fun setupSliderView(images: ArrayList<String>) {
//        sliderView = activity?.findViewById(R.id.image_slider)!!
//        sliderAdapter = SliderAdapter(images)
//        sliderView.setSliderAdapter(sliderAdapter)
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
//        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
//        sliderView.startAutoCycle()
//
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                findNavController().navigate(R.id.action_movieListFragment_to_searchMovieFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}