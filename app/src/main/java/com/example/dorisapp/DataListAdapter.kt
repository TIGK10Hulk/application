package com.example.dorisapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataListAdapter(val dataList: ArrayList<CoordinateData>): RecyclerView.Adapter<DataListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coordinateData: CoordinateData = dataList[position]

        holder.textViewXCoordinate.text = coordinateData.xCoordinateValue
        holder.textViewYCoordinate.text = coordinateData.yCoordinateValue
        holder.textViewCollision.text = coordinateData.collisionValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.data_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewXCoordinate = itemView.findViewById(R.id.xCoordinateText) as TextView
        val textViewYCoordinate = itemView.findViewById(R.id.yCoordinateText) as TextView
        val textViewCollision = itemView.findViewById(R.id.collisionText) as TextView
    }
}