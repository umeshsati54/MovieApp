package com.usati.movieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.usati.movieapp.R
import com.usati.movieapp.data.model.Result
import com.usati.movieapp.utils.Constants.Companion.IMAGE_URL
import kotlinx.android.synthetic.main.flipper_items.view.*


open class FlipperAdapter(
    private val mCtx: Context,
    private val movies: MutableList<Result>
) : BaseAdapter() {
    override fun getCount(): Int {
        return movies.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val movie: Result = movies[position]

        val inflater = LayoutInflater.from(mCtx)
        val view: View = inflater.inflate(R.layout.flipper_items, null)
        view.setOnClickListener {
            onItemClickListener?.let {
                it(movie)
            }
        }

        Glide.with(mCtx).load(IMAGE_URL + movie.backdrop_path).into(view.imageView)
        return view
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}