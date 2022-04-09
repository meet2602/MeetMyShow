package com.materialsouk.meetmyshow.models

data class OrderItem(
    val movieId: String,
    val banner_image_url: String,
    val movie_name: String,
    val cinemaName :String,
    val cinemaLocation :String,
    val quality :String,
    val price :String,
    val totalPrice :String,
    val date:String,
    val time:String,
    val seat:String
)