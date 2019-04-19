package com.test24i.moviedatabase.models

import com.google.gson.annotations.SerializedName
import com.test24i.moviedatabase.enums.BackdropSize
import com.test24i.moviedatabase.enums.PosterSize
import com.test24i.moviedatabase.utils.Consts
import java.io.Serializable
import java.lang.StringBuilder

data class Movie(val id: Int, val title: String, val overview: String,
                 @SerializedName("backdrop_path") val backdropPath: String,
                 @SerializedName("poster_path") val posterPath: String,
                 @SerializedName("release_date") val releaseDate: String,
                 @SerializedName("vote_count") val voteCount: Int,
                 @SerializedName("vote_average") val voteAverage: Float,
                 @SerializedName("original_language") val language: String?,
                 val popularity: Float, val runtime: Int?, val status: String?,
                 @SerializedName("tagline") val tagLine: String?, val genres: List<Genre>?,
                 @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>?) : Serializable {

    fun getPosterUrl(size: PosterSize) : String = Consts.IMAGES_BASE_URL + size.value + posterPath

    fun getPosterUrl() : String = getPosterUrl(posterSize)

    fun getBackdropUrl(size: BackdropSize) : String = Consts.IMAGES_BASE_URL + size.value + backdropPath

    fun getBackdropUrl() : String = getBackdropUrl(backdropSize)

    fun getGenresString() : String {
        val genresString = StringBuilder()
        val seperator = ", "
        genres?.forEach {
            if (!genresString.isEmpty()) {
                genresString.append(seperator)
            }
            genresString.append(it.name)
        }
        return genresString.toString()
    }

    fun getLanguagesString() : String {
        val languagesString = StringBuilder()
        val seperator = ", "
        spokenLanguages?.forEach {
            if (!languagesString.isEmpty()) {
                languagesString.append(seperator)
            }
            languagesString.append(it.name)
        }
        return languagesString.toString()
    }

    companion object {
        var posterSize = PosterSize.ORIGINAL
        var backdropSize = BackdropSize.ORIGINAL
    }

}