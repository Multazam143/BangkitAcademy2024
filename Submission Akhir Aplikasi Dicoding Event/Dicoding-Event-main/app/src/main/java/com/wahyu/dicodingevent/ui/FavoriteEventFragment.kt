package com.wahyu.dicodingevent.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyu.dicodingevent.database.entity.FavoriteEventEntity
import com.wahyu.dicodingevent.databinding.FragmentListEventBinding
import com.wahyu.dicodingevent.ui.tools.adapter.FavoriteEventAdapter
import com.wahyu.dicodingevent.ui.tools.factory.FavoriteFactory
import com.wahyu.dicodingevent.ui.tools.viewmodel.FavoriteEventViewModel


class FavoriteEventFragment : Fragment() {
    private var _binding: FragmentListEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteEventAdapter

    companion object {
        private const val TAG = "FavoriteEventFragment"
    }

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

        val factory: FavoriteFactory = FavoriteFactory.getInstance(requireActivity())
        val viewModel: FavoriteEventViewModel by viewModels {
            factory
        }

        viewModel.getFavoriteEvent().observe(viewLifecycleOwner) { result ->
            if (result.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Belum ada item favorite", Toast.LENGTH_SHORT).show()
            } else {
                setRecyclerView(result)
            }
            Log.d(TAG, "OnResume: $result")
        }
    }

    override fun onResume() {
        super.onResume()

        val factory: FavoriteFactory = FavoriteFactory.getInstance(requireActivity())
        val viewModel: FavoriteEventViewModel by viewModels {
            factory
        }

        viewModel.getFavoriteEvent().observe(viewLifecycleOwner) { result ->
            setRecyclerView(result)
            if (result.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Belum ada item favorite", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "OnResume: $result")
        }
    }

    private fun setRecyclerView(listEvents: List<FavoriteEventEntity>) {
        favoriteAdapter = FavoriteEventAdapter()
        favoriteAdapter.submitList(listEvents)
        binding.rvListEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}