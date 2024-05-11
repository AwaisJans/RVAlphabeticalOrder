package com.jans.rv.sample.ordered.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.activities.prctse.libAlphabeticalScroller.RecyclerViewFastScroller
import com.jans.rv.sample.ordered.app.activities.prctse.libStickyHeader.StickyHeaderInterface
import com.jans.rv.sample.ordered.app.models.NamesModel
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.MALE_TYPE

class NamesAdapter(private var productsList: List<NamesModel.NamesModelItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    RecyclerViewFastScroller.BubbleTextGetter
    , StickyHeaderInterface
{

    private lateinit var mContext: Context
    private val VIEW_TYPE_HEADER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                ItemsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_names, parent, false
                ))
            }

            else -> {
                ItemsVH(LayoutInflater.from(parent.context).inflate(
                    R.layout.header1_item_recycler, parent, false
                ))
            }
        }


    }


    override fun getItemViewType(position: Int): Int {
        return 1
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

    override fun getTextToShowInBubble(pos: Int): String {
        if (pos < 0 || pos >= productsList!!.size)
            return null!!

        val name: String = productsList!![pos].firstname
        if (name.isEmpty())
            return null!!

//        if(pos == 0 &&
//            productsList!![pos].firstname.substring(0, 1) == "B"){
//            Toast.makeText(mContext, productsList!![pos].firstname.substring(0, 1), Toast.LENGTH_SHORT).show()
//        }

        return productsList!![pos].firstname.substring(0, 1)




    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var position = itemPosition
        do {
            if (this.isHeader(position)) {
                headerPosition = position
                break
            }
            position -= 1
        } while (position >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
            return R.layout.header1_item_recycler
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        (header as TextView).text = productsList!![headerPosition].firstname.substring(0, 1)
    }

    override fun isHeader(itemPosition: Int): Boolean {
        if (itemPosition % 6 == 0){
            return true
        } else {
            return false
        }
    }
}


















