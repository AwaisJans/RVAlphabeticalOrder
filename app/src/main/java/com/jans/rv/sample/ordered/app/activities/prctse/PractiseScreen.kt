package com.jans.rv.sample.ordered.app.activities.prctse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jans.rv.sample.ordered.app.databinding.PractiseActivityBinding


class PractiseScreen : AppCompatActivity() {
    lateinit var binding: PractiseActivityBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PractiseActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }



}
