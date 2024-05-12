package com.jans.rv.sample.ordered.app.libAlphabeticalScroller

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.R

class AlphabetAdapter(private val mContext: Context, private var mDataArray: List<AlphabetItem>?) :
    RecyclerView.Adapter<AlphabetAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun refreshDataChange(newDataSet: List<AlphabetItem>?) {
        this.mDataArray = newDataSet
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (mDataArray == null) return 0
        return mDataArray!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val alphabet = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alphabet_layout, parent, false)
        return ViewHolder(alphabet)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDataArray!![position], position)
    }

    interface OnItemClickListener {
        fun OnItemClicked(alphabetPosition: Int, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvWord: TextView = itemView.findViewById<View>(R.id.tv_word) as TextView

        fun bind(alphabetItem: AlphabetItem?, position: Int) {
            if (alphabetItem?.word == null) return

            // Text
            tvWord.text = alphabetItem.word
            // Style
            tvWord.setTypeface(null, if (alphabetItem.isActive) Typeface.BOLD else Typeface.NORMAL)
            // Text color
            tvWord.setTextColor(
                if (alphabetItem.isActive
                ) mContext.resources.getColor(R.color.alphabet_text_selected_color)
                else mContext.resources.getColor(R.color.black)
            )
            tvWord.setBackgroundColor(
                mContext.resources.getColor(
                    android.R.color.transparent,
                    null
                )
            )
            // Click event
            tvWord.setOnClickListener(View.OnClickListener {
                if (listener == null) {
                    return@OnClickListener
                }
                listener!!.OnItemClicked(alphabetItem.position, position)
            })
        }
    }
}