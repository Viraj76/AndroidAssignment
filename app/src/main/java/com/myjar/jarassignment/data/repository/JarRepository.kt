package com.myjar.jarassignment.data.repository

import com.myjar.jarassignment.data.api.ApiService
import com.myjar.jarassignment.data.model.ComputerItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

interface JarRepository {
    suspend fun fetchResults(): Flow<List<ComputerItem>>
}

class JarRepositoryImpl(
    private val apiService: ApiService
) : JarRepository {
    override suspend fun fetchResults(): Flow<List<ComputerItem>> = callbackFlow {
        trySend(apiService.fetchResults())
        awaitClose{}
    }
}