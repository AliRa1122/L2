package com.example.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.app.repositories.AuthRepository
import com.example.app.viewmodels.LoginActivityViewModel
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
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginActivityViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Rule to allow LiveData to work synchronously

    private val testDispatcher = TestCoroutineDispatcher() // Dispatcher for controlling coroutine execution in tests

    @Mock
    private lateinit var authRepository: AuthRepository // Mocked AuthRepository for testing

    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean> // Observer for loading state

    @Mock
    private lateinit var errorMessageObserver: Observer<String?> // Observer for error messages

    @Mock
    private lateinit var keypassObserver: Observer<String?> // Observer for the keypass

    private lateinit var viewModel: LoginActivityViewModel // ViewModel to be tested

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito annotations
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher
        viewModel = LoginActivityViewModel(authRepository) // Initialize ViewModel with mocked repository
        viewModel.getIsLoading().observeForever(isLoadingObserver) // Observe loading state
        viewModel.getErrorMessage().observeForever(errorMessageObserver) // Observe error messages
        viewModel.getKeyPass().observeForever(keypassObserver) // Observe keypass
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original one
        testDispatcher.cleanupTestCoroutines() // Clean up coroutines
    }

    @Test
    fun `loginUser success should update keypass and clear error message`() = runBlockingTest {
        // Given
        val username = "ali"
        val password = "s4643522"
        val keypass = "animals"
        `when`(authRepository.loginUser(username, password)).thenReturn(flowOf(Result.success(keypass))) // Mock successful login response

        // When
        viewModel.loginUser(username, password) // Trigger login

        // Then
        verify(keypassObserver).onChanged(keypass) // Verify keypass is updated
        verify(errorMessageObserver).onChanged("") // Verify error message is cleared
    }

    @Test
    fun `loginUser failure should update error message and clear keypass`() = runBlockingTest {
        // Given
        val username = "testuser"
        val password = "testpass"
        val errorMessage = "Login failed"
        `when`(authRepository.loginUser(username, password)).thenReturn(flowOf(Result.failure(Exception(errorMessage)))) // Mock login failure response

        // When
        viewModel.loginUser(username, password) // Trigger login

        // Then
        verify(errorMessageObserver).onChanged(errorMessage) // Verify error message is updated
        verify(keypassObserver).onChanged(null) // Verify keypass is cleared
    }

    @Test
    fun `loginUser should handle unknown error`() = runBlockingTest {
        // Given
        val username = "testuser"
        val password = "testpass"
        `when`(authRepository.loginUser(username, password)).thenReturn(flowOf(Result.failure(Exception()))) // Mock unknown error response

        // When
        viewModel.loginUser(username, password) // Trigger login

        // Then
        verify(errorMessageObserver).onChanged("Unknown error occurred") // Verify unknown error message is shown
        verify(keypassObserver).onChanged(null) // Verify keypass is cleared
    }
}
