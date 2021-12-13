package com.podium.technicalchallenge


import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import okhttp3.OkHttpClient


class ApiClient {
    val API_URL = "https://podium-fe-challenge-2021.netlify.app/.netlify/functions/graphql"
    // Create a 10MB NormalizedCacheFactory
    private val cacheFactory = LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024 * 1024).build())

    companion object {
        private var INSTANCE: ApiClient? = null
        fun getInstance() = INSTANCE
            ?: ApiClient().also {
                INSTANCE = it
            }
    }

    val movieClient: ApolloClient = ApolloClient.builder()
        .serverUrl(API_URL)
        .normalizedCache(cacheFactory)
        .okHttpClient(
            OkHttpClient().newBuilder()
                .build()
        )
        .build()

}