package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.databinding.ActivityOtpactivityBinding
import java.util.*
import java.util.concurrent.TimeUnit


class OTPActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String? = null
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
        mAuth = FirebaseAuth.getInstance()
        loadingDialog = Dialog(binding.root.context)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.setCancelable(false)
        val phone = "+91" + intent.getStringExtra("phone_no")
        sendVerificationCode(phone)
        binding.verifyOTPBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.edOtp.text.toString().trim())) {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(binding.edOtp.text.toString().trim())
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    binding.edOtp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OTPActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    register()
                } else {
                    Toast.makeText(
                        this@OTPActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun register() {
        loadingDialog.show()
        val registerHM: HashMap<String, Any> = HashMap()
        val emailId: String = intent.getStringExtra("email")!!
        val password: String = intent.getStringExtra("password")!!
        registerHM["username"] = intent.getStringExtra("username")!!
        registerHM["email_id"] = emailId
        registerHM["phone_no"] = intent.getStringExtra("phone_no")!!
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
                                    this@OTPActivity,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val mainIntent = Intent(
                                    this@OTPActivity,
                                    MainActivity::class.java
                                )
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(mainIntent)
                                finish()
                            } else {
                                loadingDialog.dismiss()
                                Toast.makeText(
                                    this@OTPActivity,
                                    Objects.requireNonNull(task1.exception).toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@OTPActivity,
                        Objects.requireNonNull(task.exception).toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}