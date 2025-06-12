package com.slide.test.usecase.users

import com.slide.test.core.Result
import com.slide.test.core.map
import com.slide.test.repository.UsersRepository
import com.slide.test.usecase.users.model.User
import com.slide.test.usecase.users.model.toUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Stefan Halus on 18 May 2022
 */
interface GetLatestUsersUseCase {
    fun execute(): Observable<Result<List<User>>>
}

internal class GetLatestUsersUseCaseImplementation @Inject constructor(
    private val usersRepository: UsersRepository,
    private val createTimeProvider: CreationTimeProvider
) : GetLatestUsersUseCase {

    override fun execute(): Observable<Result<List<User>>> {
        return usersRepository.getUsers(null)
            .switchMap { result ->
                Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.computation())
                    .map {
                        val now = createTimeProvider.getCurrentTime()
                        result.map { it.data.map { userModel -> userModel.toUseCase(now - createTimeProvider.getAppStartTime()) } }
                    }
            }
    }
}