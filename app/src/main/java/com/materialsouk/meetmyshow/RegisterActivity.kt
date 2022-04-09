package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.databinding.ActivityRegisterBinding
import java.util.*


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private var isValidPassword = false
    private var isValidConPassword = false
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.signUpBtn.setOnClickListener { validation() }

        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditTextPassword(binding.edPassword, binding.txtPasswordL, "password")
            }
        })
        binding.edConPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditTextPassword(binding.edConPassword, binding.txtConPasswordL, "conPassword")
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
        if (binding.edUserName.text.toString().trim().isEmpty()) {
            binding.edUserName.error = "Required"
        } else if (binding.edEmailId.text.toString().trim().isEmpty()) {
            binding.edEmailId.error = "Required"
        } else if (!validEmail(binding.edEmailId.text.toString().trim())) {
            binding.edEmailId.error = "Enter valid e-mail!"
        } else if (binding.edPhoneNum.text.toString().trim().isEmpty()) {
            binding.edPhoneNum.error = "Required"
        } else if (!validPhoneNo(binding.edPhoneNum.text.toString().trim())) {
            binding.edPhoneNum.error = "Enter valid phone no.!"
        } else {
            validateEditTextPassword(binding.edPassword, binding.txtPasswordL, "password")
            if (isValidPassword) {
                validateEditTextPassword(binding.edConPassword, binding.txtConPasswordL, "conPassword")
                if (isValidConPassword) {
                    if (binding.edPassword.text.toString().trim() != binding.edConPassword.text.toString().trim()
                    ) {
                        binding.txtConPasswordL.error = "Password don't match!"
                    } else {
                        binding.txtConPasswordL.error = null
//                        register()
                        val intent = Intent(this,OTPActivity::class.java)
                        intent.putExtra("email",binding.edEmailId.text.toString().trim())
                        intent.putExtra("password",binding.edPassword.text.toString().trim())
                        intent.putExtra("username",binding.edUserName.text.toString().trim())
                        intent.putExtra("phone_no", binding.edPhoneNum.text.toString().trim())
                        startActivity(intent)
                    }
                }
            }
        }
    }
    private fun register() {
        loadingDialog.show()
        val registerHM: HashMap<String, Any> = HashMap()
        val emailId: String =  binding.edEmailId.text.toString().trim()
        val password: String =  binding.edPassword.text.toString().trim()
        registerHM["username"] =  binding.edUserName.text.toString().trim()
        registerHM["email_id"] = emailId
        registerHM["phone_no"] = binding.edPhoneNum.text.toString().trim()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val id = FirebaseAuth.getInstance().currentUser!!.uid
                    registerHM["id"] = id
                    FirebaseFirestore.getInstance().collection("Users").document(id).set(registerHM)
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                loadingDialog.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val mainIntent = Intent(
                                    this@RegisterActivity,
                                    MainActivity::class.java
                                )
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(mainIntent)
                                finish()
                            } else {
                                loadingDialog.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    Objects.requireNonNull(task1.exception).toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        Objects.requireNonNull(task.exception).toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun validateEditTextPassword(
        editText: EditText,
        textInputLayout: TextInputLayout,
        state: String
    ) {
        var truthState = false
        when {
            editText.text.toString().trim { it <= ' ' }.isEmpty() -> {
                textInputLayout.error = "Required"
            }
            editText.text.toString().length < 8 -> {
                textInputLayout.error = "Password must be 8 character!"
            }
            else -> {
                textInputLayout.error = null
                truthState = true
            }
        }
        if (truthState) {
            if (state == "password") {
                isValidPassword = true
            } else if (state == "conPassword") {
                isValidConPassword = true
            }
        } else {
            if (state == "password") {
                isValidPassword = false
            } else if (state == "conPassword") {
                isValidConPassword = false
            }
        }
    }

    private fun validEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validPhoneNo(phoneNo: String): Boolean {
        val regex = Regex("[7-9][0-9]{9}")
        return phoneNo.matches(regex)
    }
}