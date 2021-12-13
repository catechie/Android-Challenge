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
import androidx.recyclerview.widget.RecyclerView
import com.podium.technicalchallenge.DemoViewModel
import com.podium.technicalchallenge.GetMoviesQuery
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.FragmentMovieDetailBinding
import com.podium.technicalchallenge.databinding.MovieCastBinding

class MovieDetailFragment : Fragment() {
    private var movieId = 0
    private val viewModel: DemoViewModel by activityViewModels()
    private lateinit var _binding: FragmentMovieDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.let { it ->
            it?.let { arg ->
                movieId = MovieDetailFragmentArgs.fromBundle(arg).id

                viewModel.liveMovie.observe(viewLifecycleOwner) {
                    val castAdapter = MovieCastAdapter(it.cast)
                    _binding.lvCast.adapter = castAdapter
                    _binding.lvCast.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        addItemDecoration(
                            DividerItemDecoration(
                                requireContext(),
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                    _binding.movie = it
                    _binding.director = it.director
                }
                _binding.lvCast.removeAllViews()
                _binding.lvCast.adapter=null
                viewModel.getAMovie(movieId)
            }
        }
    }
}

class MovieCastAdapter(var list: List<GetMoviesQuery.Cast>) :
    RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {
    class CastViewHolder(var view: MovieCastBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MovieCastBinding>(
            inflater,
            R.layout.movie_cast,
            parent,
            false
        )
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.view.name.text = list[position].name
        holder.view.character.text = list[position].character
    }
}
