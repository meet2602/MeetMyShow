package com.materialsouk.meetmyshow.models

data class MoviesItem(
    val movieId: String,
    val about_movie: String,
    val banner_image_url: String,
    val cover_image_url: String,
    val languages: String,
    val movie_duration: String,
    val movie_name: String,
    val rating: String,
    val release_date: String,
    val stated: Boolean = false
)