package com.example.mystory2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystory2.R
import com.example.mystory2.adapter.LoadingStateAdapter
import com.example.mystory2.adapter.StoryAdapter
import com.example.mystory2.data.local.UserPreferences
import com.example.mystory2.databinding.ActivityMainBinding
import com.example.mystory2.response.ListStoryItem
import com.example.mystory2.ui.ViewModelFactory
import com.example.mystory2.ui.add.AddActivity
import com.example.mystory2.ui.detail.DetailActivity
import com.example.mystory2.ui.login.LoginActivity
import com.example.mystory2.ui.maps.MapActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var sharedPreference: UserPreferences? = null
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]
        sharedPreference = UserPreferences(this)

        binding.btnMoveToAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }

        showData()
        val strLoginStatus = sharedPreference?.getPreferenceString("login_status")
        if (strLoginStatus == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                sharedPreference?.clearSharedPreference()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.setting ->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.maps ->{
                val intent = Intent(this@MainActivity,MapActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showData() {

        val adapter = StoryAdapter() 
        binding.rvStory.adapter = adapter.withLoadStateHeaderAndFooter(
            footer = LoadingStateAdapter {adapter.retry()},
            header = LoadingStateAdapter{adapter.retry()}
        )

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                showDetail(data)
            }
        })
        viewModel.userStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }
    private fun showDetail(data: ListStoryItem){
        val intent= Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DETAIL,data)
        startActivity(intent)
    }
    companion object{
        const val CAMERA_X_RESULT = 200
    }
}