package com.jans.rv.sample.ordered.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.models.NamesModel
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.MALE_TYPE

class NamesAdapter(private var productsList: List<NamesModel.NamesModelItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_HEADER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                ItemsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_names, parent, false
                ))
            }

            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.header_item, parent, false
                ))
            }

            else -> {
                ItemsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_names, parent, false
                ))
            }
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // getting Context
        mContext = holder.itemView.context

        val user = productsList?.get(position)


        when (holder) {
            is ItemsVH -> {
                holder.bind(user)
            }
            is HeaderViewHolder -> {
                holder.bind(user)
            }
        }




    }


    override fun getItemViewType(position: Int): Int {
        val user = productsList?.get(position)

        return VIEW_TYPE_ITEM
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind header data to the header view
        var headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

        fun bind(user: NamesModel.NamesModelItem?) {
            headerTextView.text = user!!.firstname
        }
    }


    override fun getItemCount(): Int = productsList!!.size

    class ItemsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        var addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        var imgPerson: ImageView = itemView.findViewById(R.id.ivPerson)



        fun bind(user: NamesModel.NamesModelItem?) {
            // getting data

            // set Firstname and Lastname
            titleTextView.text = "${user?.firstname}, ${user?.lastname}"
            // check if male or female
            if (user?.gender == MALE_TYPE) {
                imgPerson.setImageResource(R.drawable.male_person_svgrepo_com)
            } else {
                imgPerson.setImageResource(R.drawable.lady_4_svgrepo_com)
            }
            // set Address
            addressTextView.text = user?.address
        }






    }
}