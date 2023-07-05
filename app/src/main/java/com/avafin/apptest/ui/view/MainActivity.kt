package com.avafin.apptest.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.avafin.apptest.databinding.ActivityMainBinding
import com.avafin.apptest.ui.viewmodel.FieldsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fieldsViewModel: FieldsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fieldsViewModel.onCreate()

        fieldsViewModel.fieldsModel.observe(this, Observer {
            binding.tvQuote.text = it.toString()
            //binding.tvQuote.text = it.data.amlCheck.toString()
            //binding.tvAuthor.text = it.data.customerFirstname.toString()
        })
        fieldsViewModel.isLoading.observe(this, Observer {
            binding.loading.isVisible = it
        })

    }

}