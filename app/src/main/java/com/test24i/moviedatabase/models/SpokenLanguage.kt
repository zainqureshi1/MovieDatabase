package com.test24i.moviedatabase.models

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(@SerializedName("iso_639_1") val code: String, val name: String)