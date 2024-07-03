package com.example.tasky.feature_agenda.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.fakes.FakeAuthenticatedRemoteRepository
import com.example.tasky.fakes.FakeSessionStateManager
import com.example.tasky.util.MainCoroutineRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AgendaViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var sessionStateManager: FakeSessionStateManager
    private lateinit var authenticatedRemoteRepository: FakeAuthenticatedRemoteRepository
    private lateinit var viewModel: AgendaViewModel

    @Before
    fun setUp() {
        sessionStateManager = FakeSessionStateManager()
        authenticatedRemoteRepository = FakeAuthenticatedRemoteRepository()

        runTest {
            sessionStateManager.setName("test name")
        }

        viewModel = AgendaViewModel(
            authenticatedRemoteRepository = authenticatedRemoteRepository,
            sessionStateManager = sessionStateManager
        )
    }

    @Test
    fun logOutClicked_success_navigateToLoginScreen(): Unit = runBlocking {
        // Given
        authenticatedRemoteRepository.shouldReturnSuccess = true

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
        authenticatedRemoteRepository.shouldReturnSuccess = false

        // When
        viewModel.logOutClicked()

        // Then
        runTest {
            val state = viewModel.viewState.first()
            assertThat(state).isEqualTo(AgendaViewState.ErrorDialog(DataError.Network.NO_INTERNET))
        }
    }

    @Test
    fun check_value_profileInitials() {
        runTest {
            val result = viewModel.initials.first()
            assertThat(result).isEqualTo("TN")
        }
    }
}