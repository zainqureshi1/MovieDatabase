package com.test24i.moviedatabase.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.test24i.moviedatabase.R
import com.test24i.moviedatabase.databinding.ActivityDetailBinding
import com.test24i.moviedatabase.helpers.ApiHelper
import com.test24i.moviedatabase.models.Movie
import com.test24i.moviedatabase.utils.Consts
import com.test24i.moviedatabase.utils.hideKeyboard
import com.test24i.moviedatabase.utils.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        movie = intent.extras?.getSerializable(Consts.EXTRA_MOVIE) as? Movie
        setupViews()
        getMovieDetails()
    }

    private fun setupViews() {
        if (movie == null) {
            return
        }
        binding.movie = movie
    }

    private fun getMovieDetails() {
        if (movie == null) {
            return
        }
        ApiHelper.instance.getMovie(movieId = movie!!.id).enqueue(object : Callback<Movie> {
            override fun onResponse(
                call: Call<Movie>,
                response: Response<Movie>
            ) {
                if (response.isSuccessful) {
                    movie = response.body()
                    setupViews()
                } else {
                    binding.root.showSnackbar("Failed to fetch movie details", R.string.retry) {
                        getMovieDetails()
                    }
                }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Timber.e(t)
                binding.root.showSnackbar("Failed to fetch movie details: " + t.localizedMessage, R.string.retry) {
                    getMovieDetails()
                }
            }
        })
    }

    fun playTrailerClicked(view: View) {
        view.hideKeyboard()
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra(Consts.EXTRA_VIDEOS_RESULT, movie?.videos)
        startActivity(intent)
    }

}