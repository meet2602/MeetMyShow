package com.materialsouk.meetmyshow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.materialsouk.meetmyshow.databinding.ActivityMovieDetailsBinding


class MovieDetailsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMovieDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_details)

        Glide.with(this)
            .load(intent.getStringExtra("cover_image_url"))
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
            .into(binding.coverImage)

        binding.movieNameTxt.text = intent.getStringExtra("movie_name")
        binding.movieDescTxt.text = intent.getStringExtra("about_movie")
        binding.ratingTxt.text = intent.getStringExtra("rating") +"%"
        binding.releaseTxt.text =
            "Release Date: " + intent.getStringExtra("release_date") + "\nTime Duration: " + intent.getStringExtra(
                "movie_duration"
            )
        binding.langTxt.text = "Languages: "+ intent.getStringExtra("languages")
        val banner_image = intent.getStringExtra("banner_image_url")!!
        val movieId = intent.getStringExtra("movieId")!!
        binding.bookTicketBtn.setOnClickListener {

            val intent = Intent(this,CinemaActivity::class.java)
            intent.putExtra("movie_name",binding.movieNameTxt.text)
            intent.putExtra( "banner_image_url",banner_image)
            intent.putExtra( "movieId",movieId)
            startActivity(intent)
        }

    }
}