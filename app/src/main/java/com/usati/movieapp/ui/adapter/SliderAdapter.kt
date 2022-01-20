package com.usati.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.databinding.CoverImageBinding
import com.usati.movieapp.utils.Constants
import kotlinx.android.synthetic.main.flipper_items.view.*


class SliderAdapter(private val movies: MutableList<Result>) : SliderViewAdapter<SliderAdapter.Holder>() {
    inner class Holder(var binding: CoverImageBinding) : SliderViewAdapter.ViewHolder(binding.root)

    override fun getCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): Holder {
        val binding = CoverImageBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(viewHolder: Holder?, position: Int) {
        val movie = movies[position]
        viewHolder?.binding?.apply {
            //Glide.with(ivBanner.context).load(image).into(ivBanner)
            Glide.with(ivBanner.context).load(Constants.IMAGE_URL + movie.backdrop_path).into(ivBanner)
            ivBanner.setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }
//            setOnClickListener {
//                onItemClickListener?.let {
//                    it(movie)
//                }
//            }
        }
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}