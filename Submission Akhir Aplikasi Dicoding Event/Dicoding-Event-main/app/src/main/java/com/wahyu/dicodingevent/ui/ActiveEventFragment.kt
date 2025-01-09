package com.wahyu.dicodingevent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.databinding.FragmentListEventBinding
import com.wahyu.dicodingevent.ui.tools.adapter.ListEventAdapter
import com.wahyu.dicodingevent.ui.tools.factory.EventViewModelFactory
import com.wahyu.dicodingevent.ui.tools.viewmodel.ListEventViewModel

class ActiveEventFragment : Fragment() {
    private var _binding: FragmentListEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var listEventAdapter: ListEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListEventBinding.inflate(layoutInflater, container, false)
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
            setRecyclerView(result)
        }
    }

    override fun onResume() {
        super.onResume()

        val factory : EventViewModelFactory = EventViewModelFactory.getInstance(requireActivity())
        val viewModel : ListEventViewModel by viewModels {
            factory
        }

        viewModel.getActiveEvent().observe(viewLifecycleOwner) { result ->
            setRecyclerView(result)
        }
    }

    private fun setRecyclerView(listEvents: List<EventEntity>) {
        listEventAdapter = ListEventAdapter()
        listEventAdapter.submitList(listEvents)
        binding.rvListEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listEventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}