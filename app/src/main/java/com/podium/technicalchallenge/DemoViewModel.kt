package com.podium.technicalchallenge

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DemoViewModel: ViewModel() {
    val liveMovieListBySearch = MutableLiveData<List<GetMovieSearchResultQuery.Movie>>()
    val searchError = MutableLiveData<Boolean>()
    val TAG = "DemoViewModel"
    var liveMovieList = MutableLiveData<List<GetMoviesQuery.Movie?>>()
    var liveMovieListByGenre = MutableLiveData<List<GetMoviesQuery.Movie?>>()
    var liveDataGenres = MutableLiveData<List<String>>()
    var liveTop5Movies = MutableLiveData<List<GetMoviesQuery.Movie?>>()
    var liveMovie = MutableLiveData<GetMoviesQuery.Movie>()
    var moviesLoadingError = MutableLiveData<Boolean>()
    var moviesLoading = MutableLiveData<Boolean>()
    var genresLoadingError = MutableLiveData<Boolean>()
    var genresLoading = MutableLiveData<Boolean>()

    fun getMovies() {
        viewModelScope.launch (Dispatchers.IO){
            val result = try {
                Repo.getInstance().getMovies()
            } catch (e: Exception) {
                Result.Error(e)
            }
            when (result) {
                is Result.Success<GetMoviesQuery.Data?> -> {
                    moviesLoading.postValue(false)
                    liveMovieList.postValue(result.data?.movies!!)
                    Log.d(TAG, "movies = " + result.data)
                }
                else -> {
                    moviesLoading.postValue(false)
                    moviesLoadingError.postValue(true)
                }
            }
        }
    }
    fun getMoviesByGenre(genre: String) {
        viewModelScope.launch (Dispatchers.Default) {
            // Reading data from the store
            val data =
                ApiClient.getInstance().movieClient.apolloStore.read(GetMoviesQuery()).execute()
            val moviesByGenre = data.movies?.filter{ it?.genres?.contains(genre)!! }
            when (moviesByGenre != null) {
                true -> {
                    liveMovieListByGenre.postValue(moviesByGenre)
                    moviesLoading.postValue(false)
                }
                false -> {
                    moviesLoadingError.postValue(true)
                    moviesLoading.postValue(false)
                }
            }
        }
    }
    fun getMovieBySearch(keyword: String, genre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                Repo.getInstance().getMoviesBySearch(keyword, genre)
            } catch (e: Exception) {
                Result.Error(e)
            }
            when (result) {
                is Result.Success<GetMovieSearchResultQuery.Data?> -> {
                    searchError.postValue(false)
                    liveMovieListBySearch.postValue(result?.data?.movies as List<GetMovieSearchResultQuery.Movie>?)
                    Log.d(TAG, "movies = " + result.data)
                }
                else -> {
                    searchError.postValue(false)
                }
            }
        }
    }
    fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                Repo.getInstance().getGenres()
            } catch (e: Exception) {
                Result.Error(e)
            }
            when (result) {
                is Result.Success<GetAllGenresQuery.Data?> -> {
                    liveDataGenres.postValue(result.data?.genres)
                    genresLoading.postValue( false)
                    Log.d(TAG, "genres= " + result.data)
                }
                else -> {
                    genresLoading.postValue( false)
                    genresLoadingError.postValue( true)
                }
            }
        }
    }
    fun getTop5() {
        viewModelScope.launch(Dispatchers.Default) {
            val data =
                ApiClient.getInstance().movieClient.apolloStore.read(GetMoviesQuery()).execute()
            val moviesTop5 = data.movies?.filterIndexed { i, _ -> i < 5 }
            when (moviesTop5 != null) {
                true -> {
                    liveTop5Movies.postValue(moviesTop5)
                    moviesLoadingError.postValue(false)
                    Log.d(TAG, "top 5 movies = $moviesTop5")
                }
                false -> moviesLoadingError.postValue(true)
            }
        }
    }
    fun getAMovie(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val data =
                ApiClient.getInstance().movieClient.apolloStore.read(GetMoviesQuery()).execute()
            val moviesById = data.movies?.find { it?.id == id }
            when (moviesById != null) {
                true -> {
                    liveMovie.postValue(moviesById)
                    Log.d(TAG, "the movie found by Id: $moviesById")
                }
                false -> moviesLoadingError.postValue(true)
            }
        }
    }

}