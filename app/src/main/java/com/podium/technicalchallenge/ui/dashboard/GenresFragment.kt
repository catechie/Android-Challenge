package com.podium.technicalchallenge.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.podium.technicalchallenge.DemoViewModel
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.FragmentGenreListBinding
import com.podium.technicalchallenge.databinding.MovieListItemGenreBinding

class GenresFragment : Fragment() {

    private val viewModel: DemoViewModel by activityViewModels()
    private var binding: FragmentGenreListBinding? = null
    private lateinit var adapter: GenreListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_list, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.liveDataGenres.value == null)
            viewModel.getGenres()
        viewModel.liveDataGenres.observe(viewLifecycleOwner) {
            adapter = GenreListAdapter(it)
            binding?.rvTitles?.adapter = adapter
            binding?.rvTitles?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
        viewModel.genresLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding?.progressBar2?.visibility = View.VISIBLE
                else -> binding?.progressBar2?.visibility = View.INVISIBLE
            }
        }
        viewModel.genresLoadingError.observe(viewLifecycleOwner) {
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

class GenreListAdapter(var list: List<String>) :
    RecyclerView.Adapter<GenreListAdapter.TitleViewHolder>(), GenreClickListener {
    class TitleViewHolder(var view: MovieListItemGenreBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TitleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MovieListItemGenreBinding>(
            inflater,
            R.layout.movie_list_item_genre,
            parent,
            false
        )
        return TitleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.view.genre = list[position]
        holder.view.genreClickListener = this
    }

    override fun onClickTitle(v: View) {
        val movieGenre = v.findViewById<TextView>(R.id.tvGenreName).text.toString()
        val action = GenresFragmentDirections.actionGenresFragmentToMovieListByGenre(movieGenre)
        Navigation.findNavController(v).navigate(action)
    }
}

interface GenreClickListener {
    fun onClickTitle(v: View)
}

