package com.materialsouk.meetmyshow.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.databinding.FragmentProfileBinding
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private lateinit var loadingDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loadingDialog = Dialog(binding.root.context)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.setCancelable(false)
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.edUserName.setText(document.data!!["username"]!!.toString())
                    binding.edEmailId.setText(document.data!!["email_id"]!!.toString())
                    binding.edPhoneNum.setText(document.data!!["phone_no"]!!.toString())
                }else{
                    (binding.root.context as Activity).finish()
                }
            }
            .addOnFailureListener { exception ->
                loadingDialog.dismiss()
                Toast.makeText(
                    binding.root.context,
                    Objects.requireNonNull(exception).toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}