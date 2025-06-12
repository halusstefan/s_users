package com.slide.test.users.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.slide.test.core.Result
import com.slide.test.core.TimeFormatter
import com.slide.test.usecase.users.GetLatestUsersUseCase
import com.slide.test.usecase.users.model.User
import com.slide.test.usecase.users.model.UserStatus
import com.slide.test.users.common.UserAvatarFactory
import com.slide.test.users.details.UserAvatar
import com.slide.test.users.model.UserUI
import com.slide.test.users.rules.RxTestSchedulerRule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Stefan Halus on 23 May 2022
 */
class UsersViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var objectUnderTest: UsersViewModel

    private val loadingState = State(isLoading = true)

    private val getLatestUsersUseCase: GetLatestUsersUseCase = mock()

    private val timeFormatter: TimeFormatter = mock()

    private val userAvatarFactory: UserAvatarFactory = mock()

    private val userAvatar = UserAvatar.UserInitials("SH")

    @Before
    fun setUp() {
        whenever(timeFormatter.formatDuration(any())).doReturn("formatted_time")
        whenever(userAvatarFactory.create(any(), any())).doReturn(userAvatar)
    }


    @Test
    fun `Load users success state`() {
        // GIVEN
        val usersList =
            listOf(UserUI(1, "name", "email", userAvatar, UserStatus.ACTIVE, "formatted_time"))
        val successState = State(isIdle = false, userList = usersList)

        val getLatestUsersSubject = PublishSubject.create<Result<List<User>>>()

        val users = listOf(User(1, "name", "email", UserStatus.ACTIVE, 1000))
        whenever(getLatestUsersUseCase.execute()).doReturn(getLatestUsersSubject)

        // WHEN
        objectUnderTest = UsersViewModel(getLatestUsersUseCase, timeFormatter, userAvatarFactory)
        val testObserver = objectUnderTest.observableState.test()
        getLatestUsersSubject.onNext(Result.Loading)
        getLatestUsersSubject.onNext(Result.Success(users))
        testSchedulerRule.triggerActions()

        // THEN
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, loadingState)
        testObserver.assertValueAt(1, successState)
    }

    @Test
    fun `Load users fail state`() {
        // GIVEN
        val errorState = State(isIdle = false, errorMessage = "Error")
        whenever(getLatestUsersUseCase.execute())
            .doReturn(Observable.just(Result.Loading, Result.Error(Exception("Error"))))

        val getLatestUsersSubject = PublishSubject.create<Result<List<User>>>()
        whenever(getLatestUsersUseCase.execute()).doReturn(getLatestUsersSubject)

        // WHEN
        objectUnderTest = UsersViewModel(getLatestUsersUseCase, timeFormatter, userAvatarFactory)
        val testObserver = objectUnderTest.observableState.test()

        getLatestUsersSubject.onNext(Result.Loading)
        getLatestUsersSubject.onNext(Result.Error(Exception("Error")))
        testSchedulerRule.triggerActions()
        // THEN
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, loadingState)
        testObserver.assertValueAt(1, errorState)
    }

}