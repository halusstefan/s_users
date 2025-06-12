package com.slide.test.users.listing

import android.util.Log
import com.slide.test.core.Result
import com.slide.test.core.TimeFormatter
import com.slide.test.core.map
import com.slide.test.core_ui.mvi.BaseViewModel
import com.slide.test.core_ui.mvi.Reducer
import com.slide.test.usecase.users.GetLatestUsersUseCase
import com.slide.test.users.common.UserAvatarFactory
import com.slide.test.users.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Created by Stefan Halus on 18 May 2022
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getLatestUsersUseCase: GetLatestUsersUseCase,
    private val timeFormatter: TimeFormatter,
    private val userAvatarFactory: UserAvatarFactory,
) : BaseViewModel<Action, State>() {

    override val initialState: State = State(isIdle = true)

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                isIdle = false,
                isLoading = true,
                errorMessage = null,
                userToDelete = null
            )

            is Change.UserList -> state.copy(
                isLoading = false,
                userList = change.userList
            )

            is Change.Error -> state.copy(
                isLoading = false,
                errorMessage = change.throwable?.localizedMessage
            )

            is Change.EmptyUserList -> state.copy(
                isLoading = false,
                isEmpty = true
            )
        }
    }

    init {
        bindActions()
        dispatch(Action.LoadUserList)
    }

    private fun bindActions() {
        val allChanges = listOf(
            userListChanges()
        )

        disposables.add(
            Observable.merge(allChanges)
            .scan(initialState, reducer)
            .filter { !it.isIdle }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::accept) { Log.e("TAG", it.stackTraceToString()) })
    }

    private fun userListChanges(): Observable<Change> {
        return actions.ofType(Action.LoadUserList::class.java)
            .switchMap {
                getLatestUsersUseCase.execute()
                    .map { result ->
                        result.map { userList ->
                            userList.map { user -> user.toUI(timeFormatter, userAvatarFactory) }
                        }
                    }
                    .map<Change> {
                        when (it) {
                            is Result.Success -> Change.UserList(it.data)
                            is Result.Error -> Change.Error(it.throwable!!)
                            is Result.Loading -> Change.Loading
                        }
                    }
                    .defaultIfEmpty(Change.EmptyUserList)
                    .onErrorReturn { Change.Error(it) }
                    .startWithItem(Change.Loading)
            }
    }
}
