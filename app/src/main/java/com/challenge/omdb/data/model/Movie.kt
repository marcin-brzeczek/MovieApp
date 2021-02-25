package com.challenge.omdb.data.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "Title") val title: String,
    @Json(name = "Year") val year: String,
    @Json(name = "imdbID") val id: String,
    @Json(name = "Type") val type: String,
    @Json(name = "Poster") val imageUrl: String?,
    @Json(name = "Genre") val categories: String? = null,
    @Json(name = "Runtime") val runtime: String? = null,
    @Json(name = "imdbRating") val score: String? = null,
    @Json(name = "Plot") val description: String? = null,
    @Json(name = "Metascore") val reviews: String? = null,
    @Json(name = "imdbVotes") val popularity: String? = null,
    @Json(name = "Director") val director: String? = null,
    @Json(name = "Writer") val writer: String? = null,
    @Json(name = "Actors") val actors: String? = null
) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}
