package com.materialsouk.meetmyshow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.materialsouk.meetmyshow.adapters.CinemaAdapter
import com.materialsouk.meetmyshow.databinding.ActivityCinemaBinding
import com.materialsouk.meetmyshow.models.CinemaModel

class CinemaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCinemaBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_cinema)
        setSupportActionBar(binding.toolbar)


        val cinemaList: ArrayList<CinemaModel> =
            arrayListOf(
                CinemaModel(
                    "Milano Multiplex",
                    "Bardoli",
                    "https://content.jdmagicbox.com/comp/surat/z3/0261px261.x261.110916120607.z2z3/catalogue/milano-multiplex-bardoli-surat-cinema-halls-2cjb8wj.jpg?clr=29333d"
                ),
                CinemaModel(
                    "Inox",
                    "Surat",
                    "https://img.dtnext.in/Articles/2021/Oct/202110221444244936_Inox-Leisure-Q2-loss-widens-to-Rs-88-cr_SECVPF.gif"
                ),
                CinemaModel(
                    "Rajhans Cinemas",
                    "Kamrej",
                    "https://content3.jdmagicbox.com/comp/surat/v9/0261px261.x261.180627193418.e3v9/catalogue/rajhans-cinemas-kamrej-surat-rvm3cwgrdp.jpg?clr=660000"
                ),
                CinemaModel(
                    "Nilkanth Multiplex Cinema",
                    "Surat",
                    "https://content3.jdmagicbox.com/comp/def_content/cinema_halls/default-cinema-halls-8.jpg?clr=2d2046"
                ),
                CinemaModel(
                    "Laxmi Cine Magic",
                    "Navsari",
                    "https://content3.jdmagicbox.com/comp/navsari/55/9999pmulmumstd3955/catalogue/laxmi-cine-magic-fadvel-navsari-multiplex-cinema-halls-2pt7t8c.png"
                ),
            )
        val movie_name = intent.getStringExtra("movie_name")
        val banner_image_url = intent.getStringExtra("banner_image_url")
        val movieId = intent.getStringExtra("movieId")
        val sharedPreferences = getSharedPreferences(
            getString(R.string.app_name),
            MODE_PRIVATE
        )
        val selectedCityList:ArrayList<CinemaModel> = ArrayList()
        val locationStr = sharedPreferences.getString("location_str", null)
        if (locationStr != null) {
            if (locationStr.isNotEmpty()) {
                val lstValues: List<String> = locationStr.split(",").map { it.trim() }
                lstValues.forEach {
                    cinemaList.forEachIndexed { i, element ->
                        if (element.cinemaLocation == it) {
                         selectedCityList.add(
                             CinemaModel(element.cinemaName,
                         element.cinemaLocation,element.cinemaDrawable)
                         )
                        }
                    }
                }
            }
        }
        if (selectedCityList.isNotEmpty()){
        val cinemaAdapter = CinemaAdapter(
            this, selectedCityList,movie_name!!,banner_image_url!!,movieId!!
        )
        binding.movieRecyclerView.adapter = cinemaAdapter
        } else{
            val cinemaAdapter = CinemaAdapter(
                this, cinemaList,movie_name!!,banner_image_url!!,movieId!!
            )
            binding.movieRecyclerView.adapter = cinemaAdapter
        }
    }
}