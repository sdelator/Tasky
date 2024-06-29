package com.example.tasky.feature_agenda.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.fakes.FakeSessionStateManager
import com.example.tasky.fakes.FakeUserRemoteRepository
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AgendaViewModelTest {
    private lateinit var sessionStateManager: SessionStateManager
    private lateinit var userRemoteRepository: UserRemoteRepository
    private lateinit var viewModel: AgendaViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()

        Dispatchers.setMain(testDispatcher)
        sessionStateManager = FakeSessionStateManager()
        userRemoteRepository = FakeUserRemoteRepository()

        runTest {
            sessionStateManager.setName("test")
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
    fun logOutClicked_navigate_login() {
        // given
        // when
        viewModel.logOutClicked()

        // then
        val event = viewModel.viewEvent
        assertThat(event).isEqualTo(AgendaViewEvent.NavigateToLoginScreen)
    }
}