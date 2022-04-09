package com.materialsouk.meetmyshow.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.adapters.MovieAdapter
import com.materialsouk.meetmyshow.databinding.FragmentHomeBinding
import com.materialsouk.meetmyshow.models.MoviesItem
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieList: ArrayList<MoviesItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        database = Firebase.database
        movieList = ArrayList()
        retrieveMovie(binding)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveMovie(binding: FragmentHomeBinding) {
        val loadingDialog = Dialog(binding.root.context)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.setCancelable(false)
        loadingDialog.show()
        database.getReference("Movie").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                for (i in snapshot.children) {
                    count += 1
                    val stated = if (i.child("started").value != null) {
                        i.child("started").value
                    } else {
                        false
                    }
                    movieList.add(
                        MoviesItem(
                            count.toString(),
                            i.child("about_movie").value.toString(),
                            i.child("banner_image_url").value.toString(),
                            i.child("cover_image_url").value.toString(),
                            i.child("languages").value.toString(),
                            i.child("movie_duration").value.toString(),
                            i.child("movie_name").value.toString(),
                            i.child("rating/no_of_ratings").value.toString(),
                            i.child("release_date").value.toString(),
                            stated as Boolean
                        )
                    )
                }
                movieAdapter = MovieAdapter(binding.root.context, movieList)
                binding.movieRecyclerView.adapter = movieAdapter
                movieAdapter.notifyDataSetChanged()
                loadingDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(binding.root.context, "Something Wrong!!", Toast.LENGTH_LONG).show()
                loadingDialog.dismiss()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.app_bar_search)

        val searchView = search.actionView as SearchView
        searchView.maxWidth = android.R.attr.width
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                val checkItemModelArrayList = ArrayList<MoviesItem>()

                for (i in movieList) {
                    if (i.movie_name.lowercase(Locale.getDefault())
                            .contains(newText!!.lowercase(Locale.getDefault()))
                    ) {
                        checkItemModelArrayList.add(i)
                    }
                }
                movieAdapter = MovieAdapter(
                    binding.root.context, checkItemModelArrayList
                )
                binding.movieRecyclerView.adapter = movieAdapter
                movieAdapter.notifyDataSetChanged()
                return true
            }
        })
        super.onCreateOptionsMenu(menu, menuInflater)
    }

}