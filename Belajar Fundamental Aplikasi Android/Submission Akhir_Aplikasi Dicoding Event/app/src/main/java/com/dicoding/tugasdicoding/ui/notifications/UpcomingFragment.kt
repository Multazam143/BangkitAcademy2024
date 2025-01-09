package com.dicoding.tugasdicoding.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.projectapi.adapter.EventAdapter
import com.dicoding.tugasdicoding.databinding.FragmentNotificationsBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val progressBar = binding.progressBar
        val recyclerView = binding.rvUpcoming
        recyclerView.layoutManager = LinearLayoutManager(context)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        notificationsViewModel.events.observe(viewLifecycleOwner) { favoriteevents ->
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = EventAdapter(favoriteevents)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
