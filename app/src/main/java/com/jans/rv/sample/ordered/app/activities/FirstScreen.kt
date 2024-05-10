package com.jans.rv.sample.ordered.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jans.rv.sample.ordered.app.databinding.FirstActivityBinding
import com.jans.rv.sample.ordered.app.databinding.PractiseActivityBinding
import com.jans.rv.sample.ordered.app.utils.iosLoader.LoadingIndicatorView


//import com.jans.rv.sample.ordered.app.R


class FirstScreen : AppCompatActivity() {

    private lateinit var binding: PractiseActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PractiseActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnMitar.setOnClickListener {
            startActivity(Intent(this, MitarbeiterScreen::class.java))
        }

        binding.practiseScreenBtn.setOnClickListener {
            startActivity(Intent(this, PractiseScreen::class.java))
        }


    }


}