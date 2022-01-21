package com.usati.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.usati.movieapp.R
import com.usati.movieapp.data.model.Cast
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import kotlinx.android.synthetic.main.item_cast.view.*

class MovieCastAdapter(
    var data: MutableList<Cast>
) : RecyclerView.Adapter<MovieCastAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cast,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val cast = data[position]
        holder.itemView.apply {
            Glide.with(this).load(IMAGE_URL + cast.profile_path).into(ivCast)
            tvTitleCast.text = cast.name
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}