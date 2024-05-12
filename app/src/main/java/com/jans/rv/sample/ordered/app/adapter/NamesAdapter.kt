package com.jans.rv.sample.ordered.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.libStickyHeaders.StickyRecyclerHeadersAdapter
import com.jans.rv.sample.ordered.app.libAlphabeticalScroller.RecyclerViewFastScroller
import com.jans.rv.sample.ordered.app.models.NamesModel
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.MALE_TYPE

class NamesAdapter(private var productsList: List<NamesModel.NamesModelItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    // interface to implement for save recycler view from crash when scrolling without alphabetical scroller
    RecyclerViewFastScroller.BubbleTextGetter,
    //  interface to implement for header
    StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder?> {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ItemsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_names, parent, false))
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
        }
    }

    override fun getItemCount(): Int = productsList!!.size

    class ItemsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        var addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        var imgPerson: ImageView = itemView.findViewById(R.id.ivPerson)

        fun bind(user: NamesModel.NamesModelItem?) {
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

    // method to change alphabetical scroller same time with recycler view scrolling and change color of alphabet
    override fun getTextToShowInBubble(pos: Int): String {
        if (pos < 0 || pos >= productsList!!.size)
            return null!!
        val name: String = productsList!![pos].firstname
        if (name.isEmpty())
            return null!!
        return productsList!![pos].firstname.substring(0, 1)
    }

    // Methods for Header
    private fun getItem(position: Int): String {
        return productsList!![position].firstname
    }

    override fun getHeaderId(position: Int): Long {
        return getItem(position)[0].code.toLong()
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sticky_header, parent, false)
        return object : RecyclerView.ViewHolder(view) {
        }
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val textView = holder!!.itemView as TextView
        textView.text = getItem(position)[0].toString()
    }




}


















