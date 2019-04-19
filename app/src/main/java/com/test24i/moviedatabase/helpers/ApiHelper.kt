package com.test24i.moviedatabase.helpers

import com.google.gson.annotations.SerializedName
import com.test24i.moviedatabase.models.Movie
import com.test24i.moviedatabase.utils.Consts
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiHelper {

    companion object {
        val instance by lazy {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Consts.API_BASE_URL)
                .build()
            retrofit.create(ApiHelper::class.java)
        }
    }

    @GET("discover/movie")
    fun getMovies(@Query("api_key") apiKey: String = Consts.API_KEY,
                  //@Query("language") language: String = "en-US",
                  @Query("sort_by") sortBy: String = "popularity.desc",
                  @Query("include_adult") includeAdult: Boolean = false,
                  @Query("page") page: Int): Call<MoviesResponse>

    @GET("discover/movie")
    fun getMovies(@Query("api_key") apiKey: String = Consts.API_KEY,
                  //@Query("language") language: String = "en-US",
                  @Query("sort_by") sortBy: String = "popularity.desc",
                  @Query("include_adult") includeAdult: Boolean = false,
                  @Query("page") page: Int,
                  @Query("release_date.gte") dateGreaterThan: String,
                  @Query("release_date.lte") dateLessThan: String): Call<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int,
                 @Query("api_key") apiKey: String = Consts.API_KEY): Call<Movie>

    data class MoviesResponse(val page: Int, @SerializedName("total_results") val totalResults: Int, @SerializedName("total_pages") val totalPages: Int, val results: List<Movie>)

}