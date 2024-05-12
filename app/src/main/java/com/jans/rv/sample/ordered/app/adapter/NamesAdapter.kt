package com.jans.rv.sample.ordered.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

            val list = listOf("https://media.istockphoto.com/id/1356070782/photo/businessman-and-businesswoman-smiling-looking-at-phone.jpg?b=1&s=612x612&w=0&k=20&c=9X6bxqHOA3SK43bmFoboFDY2gbgsk__7N_I2FP1myuE=",
            "https://media.istockphoto.com/id/1333405308/photo/positive-woman-video-calling-using-laptop-at-home.jpg?b=1&s=612x612&w=0&k=20&c=Bq5BKvL0yjKpCi_Xsg_iDUyLEs8KM8ZR9Nmerdz2W98=",
            "https://media.istockphoto.com/id/1365824279/photo/black-businesswoman-sitting-at-her-desk-working-on-a-laptop-computer-smiling-successful.jpg?b=1&s=612x612&w=0&k=20&c=jU4VsRMl5vYzcwwTCy8xKjUK6ediEi_bhGFmY4bz3W4=",
           "https://media.istockphoto.com/id/1313901506/photo/cropped-shot-of-an-african-american-young-woman-using-smart-phone-at-home-smiling-african.jpg?b=1&s=612x612&w=0&k=20&c=t0ZJ3i_AZr8RWYu2Q7yPhJC6q01owZTcR4_S-WH5564=",
            "https://images.pexels.com/photos/123335/pexels-photo-123335.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://images.pexels.com/photos/8088444/pexels-photo-8088444.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://images.pexels.com/photos/3184398/pexels-photo-3184398.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://images.pexels.com/photos/3184338/pexels-photo-3184338.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://images.pexels.com/photos/3153198/pexels-photo-3153198.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://images.pexels.com/photos/927022/pexels-photo-927022.jpeg?auto=compress&cs=tinysrgb&w=800")


                if (user?.gender == MALE_TYPE) {
                        Glide.with(itemView.context)
                            .load(list[8])
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imgPerson)
                } else {
                        Glide.with(itemView.context)
                            .load(list[5])
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imgPerson)
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


















