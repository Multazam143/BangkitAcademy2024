package com.example.menuseafood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.menuseafood.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil objek MenuSeafood dari Intent
        val dataMenuSeafood = intent.getParcelableExtra<MenuSeafood>("key_Seafood")

        // Memastikan dataMenuSeafood tidak null sebelum menampilkan
        dataMenuSeafood?.let { seafood ->
            // Menampilkan dataMenuSeafood di tampilan detail
            binding.tvDetailName.text = seafood.name
            binding.ivDetailPhoto.setImageResource(seafood.photo)

            // Mengambil array nama dan detail dari resource
            val dataNames = resources.getStringArray(R.array.data_name)
            val dataDetails = resources.getStringArray(R.array.Detail)

            // Cari indeks dari nama seafood yang cocok dengan data_names
            val index = dataNames.indexOf(seafood.name)

            // Jika ditemukan, tampilkan deskripsi dari array Detail
            if (index != -1) {
                binding.tvDataDetail.text = dataDetails[index]
            } else {
                binding.tvDataDetail.text = getString(R.string.default_description)
            }
        }
    }
}
