package com.example.mystory2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystory2.databinding.ActivityDetailBinding
import com.example.mystory2.response.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL) as ListStoryItem

        with(binding){
            tvName.text=detail.name
            tvDescription.text=detail.description
            Glide.with(this@DetailActivity)
                .load(detail.photoUrl)
                .into(imgDetail)
        }
    }
    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
}