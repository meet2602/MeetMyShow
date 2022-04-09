package com.materialsouk.meetmyshow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.materialsouk.meetmyshow.BR
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.databinding.CitiesItemLayoutBinding
import com.materialsouk.meetmyshow.models.CitiesModel

class LocationAdapter(private val context: Context, private val locationList: List<CitiesModel>) :
    RecyclerView.Adapter<LocationAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val rootView = CitiesItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return BindingViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val movie = locationList[position]
        holder.itemBinding.setVariable(BR.locationItem, movie)
        holder.itemView.setOnClickListener {
            locationList[position].isChecked = !locationList[position].isChecked
            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name),
                AppCompatActivity.MODE_PRIVATE
            )
            val locationStr = StringBuilder()
            locationStr.delete(0, locationStr.length)
            for (i in locationList) {
                if (i.isChecked) {
                    locationStr.append(i.city + ",")
                }
            }
            if (locationStr.isNotEmpty()) {
                locationStr.deleteCharAt(locationStr.length - 1)
                sharedPreferences.edit().putString("location_str", locationStr.toString()).apply()
            }
            notifyItemChanged(position)
        }
        holder.itemBinding.executePendingBindings()
    }

    override fun getItemCount() = locationList.size
    class BindingViewHolder(val itemBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}