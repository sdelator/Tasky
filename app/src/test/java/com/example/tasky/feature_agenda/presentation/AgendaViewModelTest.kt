package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class AgendaViewModelTest {
    @RelaxedMockK
    private lateinit var sessionStateManager: SessionStateManager

    @RelaxedMockK
    private lateinit var userRemoteRepository: UserRemoteRepository

    private lateinit var viewModel: AgendaViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel =
            AgendaViewModel(
                userRemoteRepository = userRemoteRepository,
                sessionStateManager = sessionStateManager
            )
    }

    @Test
    fun formatInitials_given_firstName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName"

        // when
        runTest {
            viewModel.getInitials()
        }
        val initials = viewModel.initials.value

        // then
        assertEquals("FI", initials)
    }

    @Test
    fun formatInitials_given_firstLastName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName lastName"

        // when
        runTest {
            viewModel.getInitials()
        }
        val initials = viewModel.initials.value

        // then
        assertEquals("FL", initials)
    }

    @Test
    fun formatInitials_given_firstMiddleLastName() {
        // given
        coEvery { sessionStateManager.getName() } returns "firstName middleName lastName"

        // when
        runTest {
            viewModel.getInitials()
        }
        val initials = viewModel.initials.value

        // then
        assertEquals("FL", initials)
    }
}