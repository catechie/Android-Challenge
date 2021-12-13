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
import com.podium.technicalchallenge.GetMoviesQuery
import com.podium.technicalchallenge.R
import com.podium.technicalchallenge.databinding.MovieListItemBinding

class MovieListAdapter(private val list: List<GetMoviesQuery.Movie>) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(), MovieClickListener {

    class MovieViewHolder(var view: MovieListItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MovieListItemBinding>(
            inflater,
            R.layout.movie_list_item,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = list[position]
        holder.view.movieClickListener = this
    }

    override fun onMovieListItemClicked(v: View) {
        val movieId: Int = v.findViewById<TextView>(R.id.movieId).text.toString().toInt()
        val action = when (v.findFragment<Fragment>()) {
            is AllMoviesFragment -> AllMoviesFragmentDirections.actionAllMToMovieDetilFragment(
                movieId
            )
            is Top5Movies -> Top5MoviesDirections.actionTop5FToMovieDetilFragment(movieId)
            is MovieListByGenre -> MovieListByGenreDirections.actionMovieListByGenreToMovieDetailFragment(
                movieId
            )
            else -> null
        }
        if (action != null) {
            Navigation.findNavController(v).navigate(action)
        }
    }
}

interface MovieClickListener {
    fun onMovieListItemClicked(v: View)
}