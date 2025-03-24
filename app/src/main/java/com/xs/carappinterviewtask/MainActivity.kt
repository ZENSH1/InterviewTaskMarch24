package com.xs.carappinterviewtask

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xs.carappinterviewtask.databinding.ActivityMainBinding
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.handleInsets
import dagger.hilt.android.AndroidEntryPoint

//Must haves
//Search Functionality
//Show Pictures of Cars
//Show Details of Cars
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            handleInsets(v, insets)
        }

    }
}