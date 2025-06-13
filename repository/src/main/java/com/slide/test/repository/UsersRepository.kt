package com.slide.test.repository

import com.slide.test.core.Page
import com.slide.test.core.Result
import com.slide.test.core.asResult
import com.slide.test.core.di.ApplicationCoroutineScope
import com.slide.test.core.di.IoDispatcher
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
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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

    fun getUserPost(userId: Long): Flow<Result<PostModel?>>

    suspend fun getUser(userId: Long): UserModel?
}

internal class UsersRepositoryImplementation @Inject constructor(
    private val usersService: UsersService,
    private val usersApiExceptionHandler: UsersApiExceptionHandler,
    private val usersInMemCache: UsersInMemCache,
    @ApplicationCoroutineScope private val coroutineScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UsersRepository {

    override fun getUsers(page: Long?): Observable<Result<Page<UserModel>>> {
        return usersService.fetchUsers(page)
            .map { pageDto -> pageDto.toModel() }
            .doOnSuccess {
                coroutineScope.launch { usersInMemCache.putAll(it.data) }
            }
            .asResult()
            .startWith(Single.just(Result.Loading))
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

    override fun getUserPost(userId: Long): Flow<Result<PostModel?>> = flow {
        try {
            emit(Result.Loading)
            emit(
                Result.Success(
                    usersService.getUserPosts(userId)
                        .data
                        .firstOrNull()?.toModel()
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Result.Error(usersApiExceptionHandler.handleException(e)))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getUser(userId: Long): UserModel? {
        return usersInMemCache.get(userId)
    }
}