package com.dicoding.tugasdicoding.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.tugasdicoding.databinding.FragmentDashboardBinding
import com.dicoding.tugasdicoding.ui.FinishedAdapter
import com.dicoding.tugasdicoding.ui.setting.SettingModelFactory
import com.dicoding.tugasdicoding.ui.setting.SettingPreferences
import com.dicoding.tugasdicoding.ui.setting.SettingViewModel
import com.dicoding.tugasdicoding.ui.setting.dataStore

class FinishedFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: SettingViewModel by viewModels {
        SettingModelFactory(SettingPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe theme setting and apply theme on fragment start
        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        val progressBar = binding.progressBar
        val recyclerView = binding.rvFinished
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        dashboardViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            Log.d("FinishedFragment", "Observed events: $events")
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = FinishedAdapter(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}