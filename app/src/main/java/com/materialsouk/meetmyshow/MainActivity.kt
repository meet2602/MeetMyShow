package com.materialsouk.meetmyshow

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.appBarSecond.toolbar)
//        binding.appBarSecond.toolbar.title = "View Movies"

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_second)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_profile
            ), drawerLayout
        )
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }
        val headerView: View = navView.getHeaderView(0)
        FirebaseFirestore.getInstance().collection("Users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    headerView.findViewById<TextView>(R.id.txtUserName).text = task.result["username"].toString()
                    headerView.findViewById<TextView>(R.id.txtEmail).text = task.result["email_id"].toString()
                } else {
                    val error: String = task.exception!!.message.toString()
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cityMenu -> {
                startActivity(
                    Intent(this, LocationActivity::class.java)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_second)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}