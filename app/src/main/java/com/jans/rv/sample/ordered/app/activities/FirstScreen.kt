package com.jans.rv.sample.ordered.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jans.rv.sample.ordered.app.activities.prctse.PractiseScreen
import com.jans.rv.sample.ordered.app.databinding.FirstActivityBinding


//import com.jans.rv.sample.ordered.app.R


class FirstScreen : AppCompatActivity() {

    private lateinit var binding: FirstActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnMitar.setOnClickListener {
            startActivity(Intent(this, MitarbeiterScreen::class.java))
        }

        binding.practiseScreenBtn.setOnClickListener {
            startActivity(Intent(this, PractiseScreen::class.java))
        }


    }


}