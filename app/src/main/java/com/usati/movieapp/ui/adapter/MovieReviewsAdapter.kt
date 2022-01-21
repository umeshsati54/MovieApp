package com.usati.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import com.usati.movieapp.R
import com.usati.movieapp.data.model.Cast
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.data.model.ResultX
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import kotlinx.android.synthetic.main.item_cast.view.*
import kotlinx.android.synthetic.main.item_review.view.*

class MovieReviewsAdapter(
    var data: MutableList<ResultX>
) : RecyclerView.Adapter<MovieReviewsAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_review,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val reviews = data[position]
        holder.itemView.apply {
            Glide.with(this).load(IMAGE_URL + reviews.author_details.avatar_path).into(ivProfile)
            if (reviews.author != null && reviews.author != "")
                tvName.text = reviews.author
//            if (reviews.author_details.rating != null)
//                tvRating.text = reviews.author_details.rating.toString()
            tvContent.text = reviews.content
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}