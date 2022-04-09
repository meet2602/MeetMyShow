package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.materialsouk.meetmyshow.databinding.ActivityLoginBinding
import java.util.*


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)


        binding.signInBtn.setOnClickListener { v ->
            validation()
            hideKeyboard(v)
        }
        binding.createAccBtn.setOnClickListener { v ->
            hideKeyboard(v)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when {
                    binding.edPassword.text.toString().trim().isEmpty() -> {
                        binding.txtPasswordL.error = "Required"
                    }
                    binding.edPassword.text.toString().trim().length < 8 -> {
                        binding.txtPasswordL.error = "Password must be 8 character!"
                    }
                    else -> {
                        binding.txtPasswordL.error = null
                    }
                }
            }
        })
        loadingDialog = Dialog(binding.root.context)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.setCancelable(false)
    }
    private fun validation() {
        if (binding.edEmailId.text.toString().trim().isEmpty()) {
            binding.edEmailId.error = "Required"
        } else if (!validEmail(binding.edEmailId.text.toString().trim())) {
            binding.edEmailId.error = "Enter valid e-mail!"
        } else if (binding.edPassword.text.toString().trim().isEmpty()) {
            binding.txtPasswordL.error = "Required"
        } else if (binding.edPassword.text.toString().trim().length < 8) {
            binding.txtPasswordL.error = "Password must be 8 character!"
        } else {
            binding.txtPasswordL.error = null
            signIn()
        }
    }

    private fun signIn() {
        loadingDialog.show()
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(
                binding.edEmailId.text.toString(),
                binding.edPassword.text.toString()
            )
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    loadingDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        Objects.requireNonNull(task.exception).toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun validEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun hideKeyboard(view: View) {
        try {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (ignored: Exception) {
        }
    }
}