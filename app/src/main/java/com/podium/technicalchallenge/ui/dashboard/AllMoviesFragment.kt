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
import com.podium.technicalchallenge.databinding.FragmentMovieListBinding

class AllMoviesFragment : Fragment() {
    private val viewModel: DemoViewModel by activityViewModels()
    private var binding: FragmentMovieListBinding? = null
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.liveMovieList.value == null) {
            viewModel.getMovies()
        }
        setObserver()
    }

    private fun setObserver() {
        viewModel.liveMovieList.observe(viewLifecycleOwner) {
            movieListAdapter = MovieListAdapter(it as List<GetMoviesQuery.Movie>)
            binding?.rvMovieList?.adapter = movieListAdapter
            binding?.rvMovieList?.apply {
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
        viewModel.moviesLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding?.progressBar?.visibility = View.VISIBLE
                else -> binding?.progressBar?.visibility = View.INVISIBLE
            }
        }
        viewModel.moviesLoadingError.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding?.apply {
                        errorMsg.visibility = View.INVISIBLE
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