package com.jans.rv.sample.ordered.app.activities.prctse;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jans.rv.sample.ordered.app.databinding.PractiseActivityBinding;

public class prctseScreenJava extends AppCompatActivity {

    PractiseActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PractiseActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }





}
