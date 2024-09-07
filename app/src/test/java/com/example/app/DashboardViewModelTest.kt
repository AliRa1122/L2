package com.example.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.app.data.response.DashboardResponse
import com.example.app.models.Entity
import com.example.app.repositories.DashboardRepository
import com.example.app.viewmodels.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Rule to allow LiveData to work synchronously

    private val testDispatcher = TestCoroutineDispatcher() // Dispatcher for controlling coroutine execution in tests

    @Mock
    private lateinit var dashboardRepository: DashboardRepository // Mocked DashboardRepository for testing

    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean> // Observer for loading state

    @Mock
    private lateinit var errorMessageObserver: Observer<String> // Observer for error messages

    @Mock
    private lateinit var dashboardDataObserver: Observer<List<Entity>> // Observer for dashboard data

    private lateinit var viewModel: DashboardViewModel // ViewModel to be tested

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito annotations
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher
        viewModel = DashboardViewModel(dashboardRepository) // Initialize ViewModel with mocked repository
        viewModel.isLoading.observeForever(isLoadingObserver) // Observe loading state
        viewModel.errorMessage.observeForever(errorMessageObserver) // Observe error messages
        viewModel.dashboardData.observeForever(dashboardDataObserver) // Observe dashboard data
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original one
        testDispatcher.cleanupTestCoroutines() // Clean up coroutines
    }

    @Test
    fun `fetchDashboard success should update dashboard data and clear error message`() = runBlockingTest {
        // Given
        val keypass = "animals"
        val dashboardResponse = DashboardResponse(
            entities = listOf(
                Entity("African Elephant", "Loxodonta africana", "Savanna", "Herbivore", "Vulnerable", 60, "Description"),
                Entity("Giant Panda", "Ailuropoda melanoleuca", "Temperate broadleaf and mixed forests", "Herbivore", "Vulnerable", 20, "Description")
            ),
            entityTotal = 2
        )
        `when`(dashboardRepository.getDashboard(keypass)).thenReturn(flowOf(Result.success(dashboardResponse))) // Mock successful fetch response

        // When
        viewModel.fetchDashboard(keypass) // Trigger data fetch

        // Then
        verify(dashboardDataObserver).onChanged(dashboardResponse.entities) // Verify dashboard data is updated
        verify(errorMessageObserver).onChanged("") // Verify error message is cleared
    }

    @Test
    fun `fetchDashboard failure should update error message and clear dashboard data`() = runBlockingTest {
        // Given
        val keypass = "invalidKeypass"
        val errorMessage = "Failed to fetch data"
        `when`(dashboardRepository.getDashboard(keypass)).thenReturn(flowOf(Result.failure(Exception(errorMessage)))) // Mock fetch failure response

        // When
        viewModel.fetchDashboard(keypass) // Trigger data fetch

        // Then
        verify(errorMessageObserver).onChanged(errorMessage) // Verify error message is updated
        verify(dashboardDataObserver).onChanged(emptyList()) // Verify dashboard data is cleared
    }

    @Test
    fun `fetchDashboard should handle unknown error`() = runBlockingTest {
        // Given
        val keypass = "unknownKeypass"
        `when`(dashboardRepository.getDashboard(keypass)).thenReturn(flowOf(Result.failure(Exception()))) // Mock unknown error response

        // When
        viewModel.fetchDashboard(keypass) // Trigger data fetch

        // Then
        verify(errorMessageObserver).onChanged("Unknown error occurred") // Verify unknown error message is shown
        verify(dashboardDataObserver).onChanged(emptyList()) // Verify dashboard data is cleared
    }

    @Test
    fun `fetchDashboard should set loading state correctly`() = runBlockingTest {
        // Given
        val keypass = "animals"
        `when`(dashboardRepository.getDashboard(keypass)).thenReturn(flowOf(Result.success(
            DashboardResponse(emptyList(), 0)
        ))) // Mock successful fetch response with empty data

        // When
        viewModel.fetchDashboard(keypass) // Trigger data fetch

        // Then
        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(isLoadingObserver, times(2)).onChanged(captor.capture()) // Capture loading state changes
        assert(captor.allValues[0] == true) // Assert that loading state is true before data is fetched
        assert(captor.allValues[1] == false) // Assert that loading state is false after data is fetched
    }
}
