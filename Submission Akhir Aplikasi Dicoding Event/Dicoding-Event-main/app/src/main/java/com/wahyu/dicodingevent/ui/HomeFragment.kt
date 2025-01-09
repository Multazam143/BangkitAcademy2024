package com.wahyu.dicodingevent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.databinding.FragmentHomeBinding
import com.wahyu.dicodingevent.ui.tools.adapter.ListEventAdapter
import com.wahyu.dicodingevent.ui.tools.factory.EventViewModelFactory
import com.wahyu.dicodingevent.ui.tools.viewmodel.ListEventViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var activeEventAdapter: ListEventAdapter
    private lateinit var completedEventAdapter: ListEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val factory : EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel : ListEventViewModel by viewModels {
            factory
        }
        viewModel.getActiveEvent().observe(viewLifecycleOwner) { result ->
            setActiveRecyclerView(result)
        }

        viewModel.getCompletedEvent().observe(viewLifecycleOwner) { result ->
            setCompletedRecyclerView(result)
        }
    }

    override fun onResume() {
        super.onResume()

        val factory : EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel : ListEventViewModel by viewModels {
            factory
        }

        viewModel.getActiveEvent().observe(viewLifecycleOwner) { result ->
            setActiveRecyclerView(result)
        }

        viewModel.getCompletedEvent().observe(viewLifecycleOwner) { result ->
            setCompletedRecyclerView(result)
        }
    }

    private fun setActiveRecyclerView(listEventItem: List<EventEntity>) {
        activeEventAdapter = ListEventAdapter()
        activeEventAdapter.submitList(listEventItem)
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = activeEventAdapter
        }
    }

    private fun setCompletedRecyclerView(listEventItem: List<EventEntity>) {
        completedEventAdapter = ListEventAdapter()
        val limitedList = listEventItem.take(5)
        completedEventAdapter.submitList(limitedList)
        binding.rvCompletedEvent.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = completedEventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}