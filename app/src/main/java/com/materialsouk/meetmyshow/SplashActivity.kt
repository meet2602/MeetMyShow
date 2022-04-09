package com.materialsouk.meetmyshow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 500)
    }
}