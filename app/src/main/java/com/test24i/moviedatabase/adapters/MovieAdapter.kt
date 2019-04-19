package com.test24i.moviedatabase.adapters

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.test24i.moviedatabase.R
import com.test24i.moviedatabase.databinding.ItemMovieBinding
import com.test24i.moviedatabase.models.Movie

class MovieAdapter(private val clickListener: (Movie) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var movies: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MovieViewHolder(parent)

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder && movies.size > position) {
            holder.bind(movies[position], clickListener)
        }
    }

    fun update(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("movies")
        fun RecyclerView.bindMovies(movies: List<Movie>) {
            val adapter = adapter as MovieAdapter
            adapter.update(movies)
        }
    }

    class MovieViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemMovieBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_movie, parent, false)
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
            binding.movie = movie
            binding.root.setOnClickListener { clickListener(movie) }
        }
    }
}