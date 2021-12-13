package com.podium.technicalchallenge.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.podium.technicalchallenge.DemoViewModel
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.MovieSearchBinding

class MovieSearchFragment : Fragment() {
    private val viewModel: DemoViewModel by activityViewModels()
    private var binding: MovieSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.movie_search, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.liveDataGenres.value==null) {
            viewModel.getGenres()
        }
        setObserver()
    }

    private fun setObserver() {

        viewModel.liveDataGenres.observe(viewLifecycleOwner){
            ArrayAdapter<String>(
                activity?.applicationContext!!,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.add("")
                viewModel.liveDataGenres.value?.let { adapter.addAll(it) }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinner?.adapter = adapter
            }
        }
        binding?.searchWords?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding?.rvSearchResult?.removeAllViews()
                    binding?.rvSearchResult?.adapter = null
                    viewModel.getMovieBySearch(query, binding?.spinner?.selectedItem.toString())
                }
                binding?.searchWords?.clearFocus()
                Log.d("MovieSearchFragment", "query is: $query")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MovieSearchFragment", "query newTtext is: $newText")
                return true
            }
        })

        viewModel.liveMovieListBySearch.observe(viewLifecycleOwner)
        {
            binding?.rvSearchResult?.removeAllViews()
            val movieListAdapter = MovieListSearchResultAdapter(it)
            binding?.rvSearchResult?.adapter = movieListAdapter
            binding?.rvSearchResult?.apply {
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

        viewModel.searchError.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding?.apply {
                        errorMsg.visibility = View.VISIBLE
                        errorMsg.text = getString(R.string.errorMsg)
                    }
                }
                else -> binding?.errorMsg?.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}