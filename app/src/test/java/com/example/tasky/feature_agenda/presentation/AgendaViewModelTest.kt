package com.example.tasky.feature_agenda.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.fakes.FakeSessionStateManager
import com.example.tasky.fakes.FakeUserRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AgendaViewModelTest {
    private lateinit var sessionStateManager: FakeSessionStateManager
    private lateinit var userRemoteRepository: FakeUserRemoteRepository
    private lateinit var viewModel: AgendaViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()

        Dispatchers.setMain(testDispatcher)
        sessionStateManager = FakeSessionStateManager()
        userRemoteRepository = FakeUserRemoteRepository()

        runTest {
            sessionStateManager.setName("test name")
        }

        viewModel = AgendaViewModel(
            userRemoteRepository = userRemoteRepository,
            sessionStateManager = sessionStateManager
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun logOutClicked_success_navigateToLoginScreen(): Unit = runBlocking {
        // Given
        userRemoteRepository.shouldReturnSuccess = true

        // When
        viewModel.logOutClicked()

        // Then
        runTest {
            assertThat(viewModel.viewEvent.first()).isEqualTo(AgendaViewEvent.NavigateToLoginScreen)
        }
    }

    @Test
    fun logOutClicked_failure_showErrorDialog() = runBlocking {
        // Given
        userRemoteRepository.shouldReturnSuccess = false

        // When
        viewModel.logOutClicked()

        // Then
        runTest {
            val state = viewModel.viewState.first()
            assertThat(state).isEqualTo(AgendaViewState.ErrorDialog(DataError.Network.NO_INTERNET))
        }
    }
}