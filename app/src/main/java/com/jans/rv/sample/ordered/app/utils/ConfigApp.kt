package com.jans.rv.sample.ordered.app.utils

import com.jans.rv.sample.ordered.app.libAlphabeticalScroller.AlphabetItem
import com.jans.rv.sample.ordered.app.models.NamesModel

class ConfigApp {


    companion object{
       const val MALE_TYPE = "male"
       const val FEMALE_TYPE = "female"

        // method for sorting recycler view items
        fun sortNames(): Comparator<NamesModel.NamesModelItem> =
            Comparator { o1, o2 -> o1!!.firstname.compareTo(o2!!.firstname) }

        // method for sorting alphabetical Scroller items
        fun sortAlphabets(): Comparator<AlphabetItem> =
            Comparator { o1, o2 -> o1!!.word.compareTo(o2!!.word) }


    }




}