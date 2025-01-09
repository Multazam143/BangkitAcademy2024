package com.wahyu.dicodingevent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wahyu.dicodingevent.data.Result
import com.wahyu.dicodingevent.databinding.ActivityMainBinding
import com.wahyu.dicodingevent.ui.HomeFragment
import com.wahyu.dicodingevent.ui.tools.factory.MainActivityViewModelFactory
import com.wahyu.dicodingevent.ui.tools.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val factory = MainActivityViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]

        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.getFirstInstall().observe(this) { isFirstInstall ->
            when (isFirstInstall) {
                false -> {
                    loadEventData()
                    viewModel.saveFirstInstall()
                }

                true -> {}
            }
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            loadEventData()
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            if (currentFragment is HomeFragment) {
                currentFragment.binding.scrollView.canScrollVertically(-1).not()
            } else {
                false
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_event_event_available, R.id.navigation_event_finish, R.id.navigation_favorite_event, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun loadEventData() {
        viewModel.getListEvent().observe(this) { result->
            when(result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}