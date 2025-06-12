package com.slide.test.users.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.slide.test.core.Result
import com.slide.test.repository.UsersRepository
import com.slide.test.repository.model.PostModel
import com.slide.test.repository.model.UserModel
import com.slide.test.repository.model.UserStatusModel
import com.slide.test.users.common.UserAvatarFactory
import com.slide.test.users.navigation.UserDetailsDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var usersRepository: UsersRepository
    private lateinit var userAvatarFactory: UserAvatarFactory
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: UserDetailsViewModel

    private val testUserId = 123L
    private val testUserName = "Test User"
    private val testUserAvatar = UserAvatar.UserInitials("TU")
    private val mockUser =
        UserModel(testUserId, testUserName, "test@example.com", UserStatusModel.ACTIVE)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        usersRepository = mock()
        userAvatarFactory = mock()
        savedStateHandle = SavedStateHandle().apply {
            set(UserDetailsDestination.Input.userIdArg, testUserId)
        }
        whenever(userAvatarFactory.create(any(), any())).thenReturn(testUserAvatar)
    }

    private fun initViewModel() {
        viewModel = UserDetailsViewModel(
            savedStateHandle,
            usersRepository,
            userAvatarFactory
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest(testDispatcher.scheduler) {
        initViewModel()
        assertEquals(UserDetailsViewState.Loading, viewModel.viewState.value)
    }

    @Test
    fun `viewState emits Success when user and posts are loaded`() =
        runTest(testDispatcher.scheduler) {
            val mockPosts = listOf(PostModel(1, testUserId, "Post Title 1", "Post Body 1"))
            whenever(usersRepository.getUser(testUserId)).thenReturn(mockUser)
            whenever(usersRepository.getUserPosts(testUserId)).thenReturn(
                flowOf(
                    Result.Success(
                        mockPosts
                    )
                )
            )

            initViewModel()

            viewModel.viewState.test {
                assertEquals(UserDetailsViewState.Loading, awaitItem())
                val successState = awaitItem() as UserDetailsViewState.Success
                assertEquals(testUserName, successState.userName)
                assertEquals("test@example.com", successState.userEmail)
                assertEquals(testUserAvatar, successState.userImage)
                assertTrue(successState.postViewState is PostViewState.Success)
                assertEquals(
                    "Post Title 1",
                    (successState.postViewState as PostViewState.Success).post?.title
                )
                assertEquals(
                    "Post Body 1",
                    (successState.postViewState as PostViewState.Success).post?.body
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `viewState emits Success with NoPosts when user loaded but no posts`() =
        runTest(testDispatcher.scheduler) {
            whenever(usersRepository.getUser(testUserId)).thenReturn(mockUser)
            whenever(usersRepository.getUserPosts(testUserId)).thenReturn(
                flowOf(
                    Result.Success(
                        emptyList()
                    )
                )
            )

            initViewModel()

            viewModel.viewState.test {
                assertEquals(UserDetailsViewState.Loading, awaitItem())
                val successState = awaitItem() as UserDetailsViewState.Success
                assertEquals(testUserName, successState.userName)
                assertTrue(successState.postViewState is PostViewState.Success)
                assertEquals(null, (successState.postViewState as PostViewState.Success).post)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `viewState emits Error when getUser returns null`() = runTest(testDispatcher.scheduler) {
        whenever(usersRepository.getUser(testUserId)).thenReturn(null)
        whenever(usersRepository.getUserPosts(testUserId)).thenReturn(
            flowOf(
                Result.Success(
                    emptyList()
                )
            )
        )

        initViewModel()

        viewModel.viewState.test {
            assertEquals(UserDetailsViewState.Loading, awaitItem())
            val errorState = awaitItem() as UserDetailsViewState.Error
            assertEquals("User not found", errorState.message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `viewState emits Success with PostError when getUserPosts returns Error`() =
        runTest(testDispatcher.scheduler) {
            val postsErrorMessage = "Failed to load posts"
            val postsException = IOException(postsErrorMessage)
            whenever(usersRepository.getUser(testUserId)).thenReturn(mockUser)
            whenever(usersRepository.getUserPosts(testUserId)).thenReturn(
                flowOf(
                    Result.Error(
                        postsException
                    )
                )
            )

            initViewModel()

            viewModel.viewState.test {
                assertEquals(UserDetailsViewState.Loading, awaitItem())
                val successState = awaitItem() as UserDetailsViewState.Success
                assertEquals(testUserName, successState.userName)
                assertTrue(successState.postViewState is PostViewState.Error)
                assertEquals(
                    postsErrorMessage,
                    (successState.postViewState as PostViewState.Error).message
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `viewState emits Success with PostLoading when getUserPosts returns Loading`() =
        runTest(testDispatcher.scheduler) {
            whenever(usersRepository.getUser(testUserId)).thenReturn(mockUser)
            whenever(usersRepository.getUserPosts(testUserId)).thenReturn(flowOf(Result.Loading))

            initViewModel()

            viewModel.viewState.test {
                assertEquals(UserDetailsViewState.Loading, awaitItem())
                val successState = awaitItem() as UserDetailsViewState.Success
                assertEquals(testUserName, successState.userName)
                assertTrue(successState.postViewState is PostViewState.Loading)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `viewState updates correctly when posts flow emits multiple values`() =
        runTest(testDispatcher.scheduler) {
            val initialPosts = listOf(PostModel(1, testUserId, "Old Post", "Old Body"))
            val updatedPosts = listOf(PostModel(2, testUserId, "New Post", "New Body"))
            val postsFlow = MutableStateFlow<Result<List<PostModel>>>(Result.Loading)

            whenever(usersRepository.getUser(testUserId)).thenReturn(mockUser)
            whenever(usersRepository.getUserPosts(testUserId)).thenReturn(postsFlow)

            initViewModel()

            viewModel.viewState.test {
                assertEquals(UserDetailsViewState.Loading, awaitItem())

                var currentState = awaitItem() as UserDetailsViewState.Success
                assertEquals(testUserName, currentState.userName)
                assertTrue(currentState.postViewState is PostViewState.Loading)

                postsFlow.value = Result.Success(initialPosts)
                advanceUntilIdle()
                currentState = awaitItem() as UserDetailsViewState.Success
                assertEquals(
                    "Old Post",
                    (currentState.postViewState as PostViewState.Success).post?.title
                )

                postsFlow.value = Result.Success(updatedPosts)
                advanceUntilIdle()
                currentState = awaitItem() as UserDetailsViewState.Success
                assertEquals(
                    "New Post",
                    (currentState.postViewState as PostViewState.Success).post?.title
                )

                cancelAndConsumeRemainingEvents()
            }
        }
}