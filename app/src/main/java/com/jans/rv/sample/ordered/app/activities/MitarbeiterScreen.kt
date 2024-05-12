package com.jans.rv.sample.ordered.app.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.libStickyHeaders.StickyRecyclerHeadersDecoration
import com.jans.rv.sample.ordered.app.libAlphabeticalScroller.AlphabetItem
import com.jans.rv.sample.ordered.app.adapter.NamesAdapter
import com.jans.rv.sample.ordered.app.databinding.ActivityMitarbeiterBinding
import com.jans.rv.sample.ordered.app.models.NamesModel
import com.jans.rv.sample.ordered.app.models.NamesModel.NamesModelItem
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.sortAlphabets
import com.jans.rv.sample.ordered.app.utils.ConfigApp.Companion.sortNames
import com.jans.rv.sample.ordered.app.utils.iosLoader.LoadingIndicatorView
import java.util.Collections
import java.util.Scanner




class MitarbeiterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMitarbeiterBinding
    private lateinit var loadingIndicator: LoadingIndicatorView
    private lateinit var tvLoader: TextView

    private var namesList: MutableList<NamesModelItem> = ArrayList()
    private var mAlphabetItems: MutableList<AlphabetItem> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMitarbeiterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickListeners()
        setupIndicator()
        setupRVCode()
        setupAlphabeticalView()

    }

    private fun clickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAlphabeticalView() {
        // setup List Of All Alphabets
        setUpAlphabetsList()
        // make alphabetical view
        binding.fastScroller.setRecyclerView(recyclerView)
        binding.fastScroller.setUpAlphabet(mAlphabetItems)
    }

    private fun setUpAlphabetsList(){
        mAlphabetItems = ArrayList()
        val strAlphabets: MutableList<String> = ArrayList()
        val list = mAlphabetItems as ArrayList<AlphabetItem>

        // Sample List that has All Alphabets
        val characters = ('a'..'z').toList()
        val uppercaseCharacters = characters.map { it.uppercase() }

        // Adding All Alphabets According to the Items
        for (i in namesList.indices) {
            val name = namesList[i].firstname
            if (name.trim { it <= ' ' }.isEmpty())
                continue
            val word = name.substring(0, 1)
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word)
                Log.d("Response123", "$word $i")
                list.add(
                    AlphabetItem(
                        i,
                        word,
                        false
                    )
                )
            }
        }
        // Adding Alphabets that are skipped
        for (char in uppercaseCharacters) {
            if (!strAlphabets.contains(char)) {
                list.add(
                    AlphabetItem(
                        0,
                        char,
                        false))
            }
        }
        // Sorting Alphabets
        Collections.sort(list, sortAlphabets())
        list.add(AlphabetItem(0, "#", false))
    }

    private fun setupRVCode() {

        recyclerView = binding.recyclerView
        // Code to Populate List
        namesList = getJsonList()
        Collections.sort(namesList, sortNames())
        Log.d("Response123", "${namesList.size}")
        // Code for Setup Adapter for Recycler View
        val namesAdapter = NamesAdapter(namesList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = namesAdapter


        // Add the sticky headers decoration
        val headersDecor = StickyRecyclerHeadersDecoration(namesAdapter)
        recyclerView.addItemDecoration(headersDecor)
        namesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                headersDecor.invalidateHeaders()
            }
        })


//         Code for Loading Indicator to Hide when needed
        Handler(Looper.getMainLooper()).postDelayed({
            loadingIndicator.stopAnimating()
            tvLoader.visibility = GONE
            recyclerView.visibility = View.VISIBLE
        }, 2000)
    }

    @SuppressLint("SetTextI18n")
    private fun setupIndicator() {

        val mainLayout = binding.main
        loadingIndicator = LoadingIndicatorView(this, 70)
        tvLoader = TextView(this)

        // setting Ids
        tvLoader.id = View.generateViewId()
        loadingIndicator.id = View.generateViewId()

        // TextView
        tvLoader.text = "Loading Data"
        tvLoader.setTextColor(Color.BLACK)
        val tvLayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        tvLayoutParams.addRule(RelativeLayout.BELOW, loadingIndicator.id)
        tvLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        tvLoader.layoutParams = tvLayoutParams
        tvLoader.textSize = 20f

        // Loading Indicator
        loadingIndicator.alpha = 1.0f
        val loadingIndicatorLayoutParams = RelativeLayout.LayoutParams(
            200, 200
        )
        loadingIndicatorLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        loadingIndicator.layoutParams = loadingIndicatorLayoutParams

        // setting up to Layout
        mainLayout.addView(tvLoader)
        mainLayout.addView(loadingIndicator)
        setContentView(mainLayout)
        loadingIndicator.startAnimating()
    }

    private fun getJsonList(): MutableList<NamesModelItem> {
        val json = Scanner(
            resources.openRawResource(R.raw.names),
            "UTF-8"
        ).useDelimiter("\\A").next()
        return Gson().fromJson(json, NamesModel::class.java).toMutableList()
    }


}