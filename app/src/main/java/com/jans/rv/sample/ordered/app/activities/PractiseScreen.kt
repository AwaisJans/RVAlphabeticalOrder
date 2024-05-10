package com.jans.rv.sample.ordered.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jans.rv.sample.ordered.app.databinding.FirstActivityBinding
import com.jans.rv.sample.ordered.app.utils.iosLoader.LoadingIndicatorView


//import com.jans.rv.sample.ordered.app.R


class PractiseScreen : AppCompatActivity() {

    private lateinit var binding: FirstActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }


}