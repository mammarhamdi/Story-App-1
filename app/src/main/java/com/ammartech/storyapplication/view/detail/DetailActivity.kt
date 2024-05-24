package com.ammartech.storyapplication.view.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ammartech.storyapplication.R
import com.ammartech.storyapplication.databinding.ActivityDetailBinding
import com.ammartech.storyapplication.view.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID).toString()

        showLoading(true)
        viewModel.getSession().observe(this) { story ->
            val token = story.token
            viewModel.getDetailStory(token, id)
        }

        setupView()
    }

    private fun setupView() {
        showLoading(true)
        viewModel.detail.observe(this) {story->
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(binding.ivDetailStory)
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            showLoading(false)
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = resources.getString(R.string.app_detail_name)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}