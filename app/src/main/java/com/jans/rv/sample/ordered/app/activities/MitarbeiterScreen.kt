package com.jans.rv.sample.ordered.app.activities

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
import com.google.gson.Gson
import com.jans.rv.sample.ordered.app.R
import com.jans.rv.sample.ordered.app.adapter.NamesAdapter
import com.jans.rv.sample.ordered.app.databinding.ActivityMitarbeiterBinding
import com.jans.rv.sample.ordered.app.models.NamesModel
import com.jans.rv.sample.ordered.app.models.NamesModel.NamesModelItem
import com.jans.rv.sample.ordered.app.utils.iosLoader.LoadingIndicatorView
import java.util.Collections
import java.util.Scanner


//import com.jans.rv.sample.ordered.app.R


class MitarbeiterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMitarbeiterBinding
    private lateinit var loadingIndicator: LoadingIndicatorView
    private lateinit var tvLoader:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMitarbeiterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.btnSearch.setOnClickListener{
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }


        setupIndicator()
        setupRVCode()






    }



    private fun sortNames():Comparator<NamesModelItem> =
        Comparator { o1, o2 -> o1!!.firstname.compareTo(o2!!.firstname) }


    private fun setupRVCode() {
        Handler(Looper.getMainLooper()).postDelayed({
            loadingIndicator.stopAnimating()
            tvLoader.visibility = GONE
            val recyclerView = binding.recyclerView
            // Code to Populate List
            val namesList = getJsonList()
            Collections.sort(namesList,sortNames())
            Log.d("Response123", "${namesList.size}")
            // Code for Setup Adapter for Recycler View
            val namesAdapter = NamesAdapter(namesList)
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = namesAdapter
        }, 2000)
    }

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