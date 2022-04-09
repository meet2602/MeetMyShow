package com.materialsouk.meetmyshow.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.adapters.OrderAdapter
import com.materialsouk.meetmyshow.databinding.FragmentOrderListBinding
import com.materialsouk.meetmyshow.models.OrderItem

class OrderListFragment : Fragment() {
    private var _binding: FragmentOrderListBinding? = null

    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderList: ArrayList<OrderItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = Firebase.database
        orderList = java.util.ArrayList()
        val loadingDialog = Dialog(binding.root.context)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.setCancelable(false)
        loadingDialog.show()
        FirebaseFirestore.getInstance().collection("User_Tickets")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("Users").get().addOnSuccessListener { documentSnapshot ->
                for (i in documentSnapshot){
                   orderList.add(OrderItem(
                       i["id"].toString(),
                       i["banner_image_url"].toString(),
                       i["movie_name"].toString(),
                       i["cinema_name"].toString(),
                       i["cinema_location"].toString(),
                       i["quantity"].toString(),
                       i["price"].toString(),
                       i["total_price"].toString(),
                       i["date"].toString(),
                       i["time"].toString(),
                       i["seating_no"].toString()
                   ))
                }
                orderAdapter = OrderAdapter(binding.root.context,orderList )
                binding.orderRecyclerView.adapter = orderAdapter
                orderAdapter.notifyDataSetChanged()
                loadingDialog.dismiss()
            }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}