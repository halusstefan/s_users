package com.slide.test.repository

import com.slide.test.core.Page
import com.slide.test.core.Result
import com.slide.test.core.asResult
import com.slide.test.network.service.UsersService
import com.slide.test.repository.cache.UsersInMemCache
import com.slide.test.repository.exceptions.UsersApiExceptionHandler
import com.slide.test.repository.model.CreateUserRequestModel
import com.slide.test.repository.model.PostModel
import com.slide.test.repository.model.UserModel
import com.slide.test.repository.model.toDto
import com.slide.test.repository.model.toModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

/**
 * Created by Stefan Halus on 18 May 2022
 */
interface UsersRepository {

    fun getUsers(page: Long?): Observable<Result<Page<UserModel>>>

    fun deleteUser(userId: Long): Completable

    fun createUser(createUserRequestModel: CreateUserRequestModel): Completable

    fun getUserPosts(userId: Long): Flow<Result<List<PostModel>>>

    suspend fun getUser(userId: Long): UserModel?
}

internal class UsersRepositoryImplementation @Inject constructor(
    private val usersService: UsersService,
    private val usersApiExceptionHandler: UsersApiExceptionHandler,
    private val usersInMemCache: UsersInMemCache
) : UsersRepository {

    override fun getUsers(page: Long?): Observable<Result<Page<UserModel>>> {
        return usersService.fetchUsers(page)
            .map { pageDto -> pageDto.toModel() }
            .doOnSuccess {
                GlobalScope.launch { usersInMemCache.putAll(it.data) }
            }
            .asResult()
    }

    override fun deleteUser(userId: Long): Completable {
        return usersService.deleteUser(userId)
    }

    override fun createUser(createUserRequestModel: CreateUserRequestModel): Completable {
        return usersService.createUser(createUserRequestModel.toDto())
            .map { it.data.toModel() }
            .ignoreElement()
            .onErrorResumeNext { throwable ->
                Completable.error(usersApiExceptionHandler.handleException(throwable))
            }
    }

    override fun getUserPosts(userId: Long): Flow<Result<List<PostModel>>> = flow {
        try {
            emit(
                Result.Success(
                    usersService.getUserPosts(userId)
                        .data
                        .map { it.toModel() }
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Result.Error(usersApiExceptionHandler.handleException(e)))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUser(userId: Long): UserModel? {
        return usersInMemCache.get(userId)
    }
}