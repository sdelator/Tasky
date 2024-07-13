package com.example.tasky.feature_agenda.presentation

import android.app.Application
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.R
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.util.CalendarHelper
import com.example.tasky.common.presentation.util.toFormatted_MM_DD_YYYY
import com.example.tasky.fakes.FakeAuthenticatedRemoteRepository
import com.example.tasky.fakes.FakeSessionStateManager
import com.example.tasky.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class AgendaViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var sessionStateManager: FakeSessionStateManager
    private lateinit var authenticatedRemoteRepository: FakeAuthenticatedRemoteRepository
    private lateinit var viewModel: AgendaViewModel

    @RelaxedMockK
    private lateinit var mockApplication: Application

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sessionStateManager = FakeSessionStateManager()
        authenticatedRemoteRepository = FakeAuthenticatedRemoteRepository()

        runTest {
            sessionStateManager.setName("test name")
        }

        viewModel = AgendaViewModel(
            authenticatedRemoteRepository = authenticatedRemoteRepository,
            sessionStateManager = sessionStateManager,
            application = mockApplication
        )
    }

    @Test
    fun getInitialState() = runTest {
        val viewState = viewModel.viewState.first()
        assertThat(LocalDate.now().monthValue).isEqualTo(viewState.monthSelected)
        assertThat(LocalDate.now().dayOfMonth).isEqualTo(viewState.daySelected)
        val expectedCalendarDays =
            CalendarHelper.getCalendarDaysForMonth(LocalDate.now().year, LocalDate.now().monthValue)
        assertThat(expectedCalendarDays).isEqualTo(viewState.calendarDays)
        assertThat(viewState.headerDateText).isEqualTo(
            mockApplication.applicationContext.getString(
                R.string.today
            )
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
            assertThat(state.showErrorDialog).isEqualTo(true)
            assertThat(state.dataError).isEqualTo(DataError.Network.NO_INTERNET)
        }
    }

    @Test
    fun check_value_profileInitials() {
        runTest {
            val result = viewModel.initials
            assertThat(result).isEqualTo("TN")
        }
    }

    @Test
    fun changeDate_via_HorizontalCalendar_updateDateSelected_firstOfMonth() {
        // Given
        val year = LocalDate.now().year
        val month = LocalDate.now().monthValue
        val day = 1
        val date = LocalDate.of(year, month, day)

        // When
        viewModel.updateDateSelected(month, day, null)

        // Then
        runTest {
            val state = viewModel.viewState.first()
            assertThat(state.monthSelected).isEqualTo(month)
            assertThat(state.daySelected).isEqualTo(day)
            assertThat(state.calendarDays).isEqualTo(
                CalendarHelper.getCalendarDaysForMonth(
                    year,
                    month
                )
            )
            assertThat(state.headerDateText).isEqualTo(date.toFormatted_MM_DD_YYYY())
            assertThat(viewModel.yearSelected.value).isEqualTo(year)
        }
    }

    @Test
    fun changeDate_via_HorizontalCalendar_updateDateSelected_today() {
        // Given
        val year = LocalDate.now().year
        val month = LocalDate.now().monthValue
        val day = LocalDate.now().dayOfMonth

        // When
        viewModel.updateDateSelected(month, day, null)

        // Then
        runTest {
            val state = viewModel.viewState.first()
            assertThat(state.monthSelected).isEqualTo(month)
            assertThat(state.daySelected).isEqualTo(day)
            assertThat(state.calendarDays).isEqualTo(
                CalendarHelper.getCalendarDaysForMonth(
                    year,
                    month
                )
            )
            assertThat(state.headerDateText).isEqualTo(
                mockApplication.applicationContext.getString(
                    R.string.today
                )
            )
            assertThat(viewModel.yearSelected.value).isEqualTo(year)
        }
    }

    @Test
    fun changeDate_via_MonthToolbarDatePicker_updateDateSelected() {
        // Given
        val year = 2023
        val month = 3
        val day = 1
        val date = LocalDate.of(year, month, day)

        // When
        viewModel.updateDateSelected(month, day, year)

        // Then
        runTest {
            val state = viewModel.viewState.first()
            assertThat(state.monthSelected).isEqualTo(month)
            assertThat(state.daySelected).isEqualTo(day)
            assertThat(state.calendarDays).isEqualTo(
                CalendarHelper.getCalendarDaysForMonth(
                    year,
                    month
                )
            )
            assertThat(state.headerDateText).isEqualTo(date.toFormatted_MM_DD_YYYY())
            assertThat(viewModel.yearSelected.value).isEqualTo(year)
        }
    }
}