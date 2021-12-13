package com.podium.technicalchallenge

import com.apollographql.apollo.coroutines.await

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class Repo {

    suspend fun getMovies(): Result<GetMoviesQuery.Data?> {
        val response = ApiClient.getInstance().movieClient.query(GetMoviesQuery()).await()
        return if (response.data != null) {
            Result.Success(response.data)
        } else {
            Result.Error(java.lang.Exception())
        }
    }

    suspend fun getGenres(): Result<GetAllGenresQuery.Data?> {
        val response = ApiClient.getInstance().movieClient.query(GetAllGenresQuery()).await()
        return if (response.data != null) {
            Result.Success(response.data)
        } else {
            Result.Error(java.lang.Exception())
        }
    }

    suspend fun getMoviesBySearch(search: String, genre: String): Result<GetMovieSearchResultQuery.Data?> {
        val response = ApiClient.getInstance().movieClient.query(GetMovieSearchResultQuery(search, genre)).await()
        return if (response.data != null) {
            Result.Success(response.data)
        } else {
            Result.Error(java.lang.Exception())
        }
    }

    companion object {
        private var INSTANCE: Repo? = null
        fun getInstance() = INSTANCE
            ?: Repo().also {
                INSTANCE = it
            }
    }
}