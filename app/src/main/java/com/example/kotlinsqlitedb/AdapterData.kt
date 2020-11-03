package com.example.kotlinsqlitedb

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsqlitedb.Activity.DetailActivity
import com.squareup.picasso.Picasso

class AdapterData() : RecyclerView.Adapter<AdapterData.foodHolder>(){

    private var context: Context?=null
    private var dataList:ArrayList<ModelData>?=null


    constructor(context: Context?, dataList: ArrayList<ModelData>?) : this() {
        this.context = context
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodHolder {

        return foodHolder(
            LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: foodHolder, position: Int) {
        val model = dataList!!.get(position)

        val id = model.id
        val name = model.name
        val price = model.price
        val description = model.description
        val addTimestamp = model.addTimeStamp
        val updateTimestamp = model.updateTimeStamp
        val image = model.image

        holder.foodNameTv.text = name
        try {
            Picasso.get().load(image).placeholder(R.drawable.ic_launcher_background)
                .into(holder.foodIv)
        } catch (e: Exception) {
            holder.foodIv.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("name",name)
            intent.putExtra("price",price)
            intent.putExtra("description",description)
            intent.putExtra("image",image)
            intent.putExtra("addTimestamp",addTimestamp)
            intent.putExtra("updateTimestamp",updateTimestamp)
            context!!.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
       return dataList!!.size
    }




    inner class foodHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var foodIv: ImageView = itemView.findViewById(R.id.foodIv)
        var foodNameTv:TextView = itemView.findViewById(R.id.foodNameTv)

    }


}