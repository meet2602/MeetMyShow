package com.materialsouk.meetmyshow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.databinding.ViewOrderLayoutBinding
import com.materialsouk.meetmyshow.models.OrderItem

class OrderAdapter(private val context: Context, private val orderList: ArrayList<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView = ViewOrderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return BindingViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val order = orderList[position]
        Glide.with(context)
            .load(order.banner_image_url)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
            .into(holder.itemBinding.roundShapeImageview)
        holder.itemBinding.txtCinemaLocation.text = "Location: ${order.cinemaLocation}"
        holder.itemBinding.txtCinemaName.text = "Cinema Name: ${order.cinemaName}"
        holder.itemBinding.txtMovieName.text = "Movie Name: ${order.movie_name}"
        holder.itemBinding.txtDate.text = "Date: ${order.date}"
        holder.itemBinding.txtTime.text = "Time: ${order.time}"
        holder.itemBinding.txtseat.text = "Seat No: ${order.seat}"
        holder.itemBinding.txtPrice.text = "Price: ${order.price}"
        holder.itemBinding.txtQuality.text = "Quality: ${order.quality}"
        holder.itemBinding.txtTotalPrice.text = "Total Price: ${order.totalPrice}"
        holder.itemBinding.deleteBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection("User_Tickets")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("Users")
                .document(order.movieId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Successfully deleted!",
                        Toast.LENGTH_LONG
                    ).show()
                    orderList.removeAt(position)
                    notifyItemRangeRemoved(position, orderList.size)
                    notifyItemRangeChanged(position, orderList.size)
                    notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                }

        }
        holder.itemBinding.executePendingBindings()
    }

    override fun getItemCount() = orderList.size
    class BindingViewHolder(val itemBinding: ViewOrderLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


}