package com.podium.technicalchallenge.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.podium.technicalchallenge.DemoViewModel
import com.podium.technicalchallenge.GetMoviesQuery
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.FragmentTop5Binding

class Top5Movies : Fragment() {
    private val viewModel: DemoViewModel by activityViewModels()
    private lateinit var binding: FragmentTop5Binding
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top5, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.liveTop5Movies.value == null)
            viewModel.getTop5()
        viewModel.liveTop5Movies.observe(viewLifecycleOwner) {
            adapter = MovieListAdapter(it as List<GetMoviesQuery.Movie>)
            binding.rvTop5.adapter = adapter
            binding.rvTop5.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )

            }
        }

    }

}

