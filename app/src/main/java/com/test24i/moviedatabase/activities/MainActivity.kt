package com.test24i.moviedatabase.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import com.test24i.moviedatabase.R
import com.test24i.moviedatabase.adapters.MovieAdapter
import com.test24i.moviedatabase.databinding.ActivityMainBinding
import com.test24i.moviedatabase.enums.BackdropSize
import com.test24i.moviedatabase.enums.PosterSize
import com.test24i.moviedatabase.models.Movie
import com.test24i.moviedatabase.models.MovieViewModel
import com.test24i.moviedatabase.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var endlessScroll: EndlessScroll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        setupViews()
        findSuitableImageSizes()
    }

    private fun setupViews() {
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        recyclerViewMovies.layoutManager = layoutManager()
        movieAdapter = MovieAdapter { movie : Movie -> movieClicked(movie) }
        recyclerViewMovies.adapter = movieAdapter
        recyclerViewMovies.addItemDecoration(GridItemDecoration(10, 3))
        endlessScroll = recyclerViewMovies.endlessScroll { viewModel.fetchMovies() }

        viewModel.movies().observe(this, Observer {
            movieAdapter.notifyDataSetChanged()
        })

        viewModel.loading.observe(this, Observer {
            if (it == true) {
                if (movieAdapter.itemCount == 0) {
                    swipeRefreshLayoutMovies.isRefreshing = true
                }
            } else if (swipeRefreshLayoutMovies.isRefreshing) {
                swipeRefreshLayoutMovies.isRefreshing = false
            }
        })

        swipeRefreshLayoutMovies.setOnRefreshListener {
            viewModel.apply {
                reset()
                fetchMovies()
            }
            endlessScroll.reset()
        }

        binding.moviesViewModel = viewModel
    }

    private fun layoutManager() : RecyclerView.LayoutManager {
        if (isTablet()) {
            return GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        }
        return GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
    }

    private fun findSuitableImageSizes() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        when (screenWidth / 3 - 10 * 2) {
            in 0..92 -> Movie.posterSize = PosterSize.W92
            in 93..154 -> Movie.posterSize = PosterSize.W154
            in 155..185 -> Movie.posterSize = PosterSize.W185
            in 186..342 -> Movie.posterSize = PosterSize.W342
            in 343..500 -> Movie.posterSize = PosterSize.W500
            in 501..780 -> Movie.posterSize = PosterSize.W780
            else -> Movie.posterSize = PosterSize.ORIGINAL
        }
        when (screenWidth) {
            in 0..300 -> Movie.backdropSize = BackdropSize.W300
            in 301..780 -> Movie.backdropSize = BackdropSize.W780
            in 781..1280 -> Movie.backdropSize = BackdropSize.W1280
            else -> Movie.backdropSize = BackdropSize.ORIGINAL
        }
    }

    private fun movieClicked(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Consts.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    fun getMoviesClicked(view: View) {
        view.hideKeyboard()
        val days = binding.editTextDays.text.toString().toIntOrNull()
        if (days != null) {
            val calendar = Calendar.getInstance()
            val dateNow = Utilities.DateUtils.dateToString(calendar.time)
            calendar.add(Calendar.DATE, -days)
            val dateBefore = Utilities.DateUtils.dateToString(calendar.time)
            viewModel.dateLessThan = dateNow
            viewModel.dateGreaterThan = dateBefore
            viewModel.fetchMovies()
        }
    }

}
