package com.test24i.moviedatabase.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.test24i.moviedatabase.helpers.ApiHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MovieViewModel : ViewModel() {
    private var pageToLoad = 1
    private val _movies = arrayListOf<Movie>()

    var dateGreaterThan: String = ""
    var dateLessThan: String = ""

    val loading = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val movies = MutableLiveData<ArrayList<Movie>>().apply {
        value = _movies
    }

    fun movies(): LiveData<ArrayList<Movie>> {
        return movies
    }

    fun fetchMovies() {
        if (loading.value == true) return
        loading.value = true
        if (dateGreaterThan.isNotEmpty() && dateLessThan.isNotEmpty()) {
            ApiHelper.instance.getMovies(
                page = pageToLoad,
                dateGreaterThan = dateGreaterThan,
                dateLessThan = dateLessThan
            ).enqueue(object : Callback<ApiHelper.MoviesResponse> {
                override fun onResponse(
                    call: Call<ApiHelper.MoviesResponse>,
                    response: Response<ApiHelper.MoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val newMovies = response.body()?.results
                        if (newMovies != null) {
                            _movies.addAll(newMovies)
                            movies.postValue(_movies)
                        }
                        pageToLoad += 1
                    }
                    loading.value = false
                }

                override fun onFailure(call: Call<ApiHelper.MoviesResponse>, t: Throwable) {
                    Timber.e(t)
                    loading.value = false
                }
            })
        } else {
            ApiHelper.instance.getMovies(page = pageToLoad).enqueue(object : Callback<ApiHelper.MoviesResponse> {
                override fun onResponse(
                    call: Call<ApiHelper.MoviesResponse>,
                    response: Response<ApiHelper.MoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val newMovies = response.body()?.results
                        if (newMovies != null) {
                            _movies.addAll(newMovies)
                            movies.postValue(_movies)
                        }
                        pageToLoad += 1
                    }
                    loading.value = false
                }

                override fun onFailure(call: Call<ApiHelper.MoviesResponse>, t: Throwable) {
                    Timber.e(t)
                    loading.value = false
                }
            })
        }
    }

    fun reset() {
        pageToLoad = 1
        _movies.clear()
    }

}