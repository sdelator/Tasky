package com.example.tasky.feature_agenda.presentation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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
    @RelaxedMockK
    private lateinit var sessionStateManager: SessionStateManager

    @RelaxedMockK
    private lateinit var userRemoteRepository: UserRemoteRepository

    private lateinit var viewModel: AgendaViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()

        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel =
            AgendaViewModel(
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
    fun formatInitials_given_firstName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName"

        // when
        runTest {
            viewModel
        }

        // then
        val initials = viewModel.initials.value
        assertThat(initials).isEqualTo("FI")
    }

    @Test
    fun formatInitials_given_firstLastName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName lastName"

        // when
        runTest {
            viewModel
        }
        val initials = viewModel.initials.value

        // then
        assertThat(initials).isEqualTo("FL")
    }

    @Test
    fun formatInitials_given_firstMiddleLastName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName middleName lastName"

        // when
        runTest {
            viewModel
        }
        val initials = viewModel.initials.value

        // then
        assertThat(initials).isEqualTo("FL")
    }
}