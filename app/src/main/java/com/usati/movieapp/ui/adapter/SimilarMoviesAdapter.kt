package com.usati.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import com.usati.movieapp.R
import com.usati.movieapp.data.model.Result
import kotlinx.android.synthetic.main.item_movie_preview.view.*

class SimilarMoviesAdapter(
    var data: MutableList<Result>
) : RecyclerView.Adapter<SimilarMoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_similar_movies,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = data[position]
        holder.itemView.apply {
            Glide.with(this).load(IMAGE_URL + movie.poster_path).into(ivPoster)
            tvTitle.text = movie.title
            setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}