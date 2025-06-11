package com.slide.test.repository.cache

import com.slide.test.repository.model.UserModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Created by Stefan Halus on 12 Jun 2025
 */

interface UsersInMemCache {
    suspend fun putAll(users: List<UserModel>): Boolean

    suspend fun get(userId: Long): UserModel?
}

class UsersInMemCacheImplementation @Inject constructor() : UsersInMemCache {
    val userList = mutableListOf<UserModel>()

    override suspend fun putAll(users: List<UserModel>) = coroutineScope {
        userList.clear()
        userList.addAll(users)
    }

    override suspend fun get(userId: Long): UserModel? = coroutineScope {
        userList.firstOrNull { it.id == userId }
    }
}