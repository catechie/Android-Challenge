package com.podium.technicalchallenge.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.podium.technicalchallenge.GetMovieSearchResultQuery
import com.podium.technicalchallenge.GetMoviesQuery
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.MovieListItemBinding
import com.podium.technicalchallenge.databinding.MovieListItemSearchBinding

class MovieListSearchResultAdapter(private val list: List<Any>) :
    RecyclerView.Adapter<MovieListSearchResultAdapter.MovieViewHolder>(), MovieSearchClickListener{

    class MovieViewHolder(var view: MovieListItemSearchBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MovieListItemSearchBinding>(
            inflater,
            R.layout.movie_list_item_search,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = list[position] as GetMovieSearchResultQuery.Movie
        holder.view.movieClickListener = this
    }

    override fun onMovieListItemClicked(v: View) {
        val movieId: Int = v.findViewById<TextView>(R.id.movieId).text.toString().toInt()
        val action = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailFragment(movieId)
        if (action != null) {
            Navigation.findNavController(v).navigate(action)
        }
    }
}

interface MovieSearchClickListener {
    fun onMovieListItemClicked(v: View)
}