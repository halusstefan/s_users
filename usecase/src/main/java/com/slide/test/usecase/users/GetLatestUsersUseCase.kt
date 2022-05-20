package com.slide.test.usecase.users

import com.slide.test.core.PageMetadata
import com.slide.test.core.Result
import com.slide.test.core.map
import com.slide.test.repository.UsersRepository
import com.slide.test.usecase.users.model.User
import com.slide.test.usecase.users.model.toUseCase
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

/**
 * Created by Stefan Halus on 18 May 2022
 */
interface GetLatestUsersUseCase {
    fun execute(): Observable<Result<List<User>>>
}

internal class GetLatestUsersUseCaseImplementation @Inject constructor(
    private val usersRepository: UsersRepository
) : GetLatestUsersUseCase {
    val currentTime = System.currentTimeMillis()
    override fun execute(): Observable<Result<List<User>>> {
        return usersRepository.getUsers(null)
            .flatMap { usersResource ->
                when (usersResource) {
                    is Result.Success -> getLastPage(usersResource.data.meta)
                    is Result.Error -> Observable.just(usersResource)
                    is Result.Loading -> Observable.just(Result.Loading)
                }
            }
    }

    @OptIn(ExperimentalTime::class)
    private fun getLastPage(metadata: PageMetadata): Observable<Result<List<User>>> {
        val lastPage = metadata.pages
        return usersRepository.getUsers(lastPage)
            .switchMap { result ->
                Observable.interval(1, TimeUnit.SECONDS)
                    .map {
                        val now = System.currentTimeMillis()
                        result.map { it.data.map { userModel -> userModel.toUseCase(now - currentTime) } }
                    }
            }
    }
}