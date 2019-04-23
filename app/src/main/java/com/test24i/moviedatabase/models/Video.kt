package com.test24i.moviedatabase.models

import java.io.Serializable

data class Video(val id: String, val name: String, val key: String, val type: String) : Serializable


data class VideosResult(val results: List<Video>) : Serializable {
    fun isEmpty() = results.isEmpty()
    fun keys(onlyTrailers: Boolean) = results.filter { !onlyTrailers || it.type.equals("trailer", true) }.map { it.key }
}
