package com.jans.rv.sample.ordered.app.activities.prctse.libStickyHeader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.TYPE_HEADER
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.TYPE_ITEM

class UserAdapter(val users: List<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderInterface  {

    override fun isHeader(itemPosition: Int): Boolean {
        return users[itemPosition].header
    }

    override fun bindHeaderData(header: View, headerPosition: Int) {
        (header as TextView).text = users[headerPosition].name
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.header1_item_recycler
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_HEADER) {
            ViewHolderHeader(LayoutInflater.from(parent.context)
                    .inflate(R.layout.header1_item_recycler, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recycler, parent, false))
        }
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.nameView.text = users[position].name
        } else if(holder is ViewHolderHeader) {
            holder.headerView.text = users[position].name
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if(users[position].header) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.tvRows)
    }

    class ViewHolderHeader(view: View) : RecyclerView.ViewHolder(view) {
        val headerView: TextView = view.findViewById(R.id.headerTextView)
    }

}