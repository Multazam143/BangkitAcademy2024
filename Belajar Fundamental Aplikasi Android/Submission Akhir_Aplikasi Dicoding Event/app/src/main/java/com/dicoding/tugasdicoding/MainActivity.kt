package com.dicoding.tugasdicoding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.tugasdicoding.databinding.ActivityMainBinding
import com.dicoding.tugasdicoding.ui.setting.SettingModelFactory
import com.dicoding.tugasdicoding.ui.setting.SettingPreferences
import com.dicoding.tugasdicoding.ui.setting.SettingViewModel
import com.dicoding.tugasdicoding.ui.setting.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Inisialisasi SettingViewModel dengan DataStore
    private val settingViewModel: SettingViewModel by viewModels {
        SettingModelFactory(SettingPreferences.getInstance(applicationContext.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observasi preferensi tema dan terapkan sebelum layout di-inflate
        lifecycleScope.launch {
            settingViewModel.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // Properly casting to NavHostFragment to get the NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the AppBarConfiguration with the top-level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_Upcoming, R.id.navigation_Finishead, R.id.navigation_favorite,
                R.id.navigation_setting
            )
        )

        // Set up the action bar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
