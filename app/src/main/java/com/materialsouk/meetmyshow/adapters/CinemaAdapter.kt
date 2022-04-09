package com.materialsouk.meetmyshow.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.SeatingActivity
import com.materialsouk.meetmyshow.databinding.ViewCinemaLayoutBinding
import com.materialsouk.meetmyshow.models.CinemaModel

class CinemaAdapter(
    private val context: Context,
    private val movieList: List<CinemaModel>,
    private val movieName: String,
    private val banner_image_url: String,
    private val movieId: String
) :
    RecyclerView.Adapter<CinemaAdapter.BindingViewHolder>() {

    private val price = arrayOf(110, 200, 100, 250, 300)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView = ViewCinemaLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return BindingViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val movie = movieList[position]
        Glide.with(context)
            .load(movie.cinemaDrawable)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
            .into(holder.itemBinding.ivGroupFirstImg)
        holder.itemBinding.txtCinemaName.text = movie.cinemaName
        holder.itemBinding.txtCinemaLocation.text = movie.cinemaLocation
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SeatingActivity::class.java)
//            intent.putExtra( "about_movie",movie.about_movie)
//            intent.putExtra( "cover_image_url",movie.cover_image_url)
//            intent.putExtra( "languages",movie.languages)
//            intent.putExtra( "movie_duration",movie.movie_duration)
            intent.putExtra("movie_name", movieName)
            intent.putExtra("movieId", movieId)
            intent.putExtra("price", price[position])
            intent.putExtra("banner_image_url", banner_image_url)
            intent.putExtra("cinema_name", movie.cinemaName)
            intent.putExtra("cinema_location", movie.cinemaLocation)
            intent.putExtra("cinema_drawable", movie.cinemaDrawable)
//            intent.putExtra( "rating",movie.rating)
//            intent.putExtra( "release_date",movie.release_date)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = movieList.size
    class BindingViewHolder(val itemBinding: ViewCinemaLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}