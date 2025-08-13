package com.example.feature.movies.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.movies.presentation.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    // In a real app, you would inject these repositories
    // private val movieRepository: MovieRepository = MovieRepository()
    // private val userRepository: UserRepository = UserRepository()

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                // Simulate API call delay
                delay(1000)

                // In a real app, this would be: movieRepository.getMovieDetail(movieId)
                val movieDetail = getSampleMovieDetail()

                // Also load user's favorite/watchlist status
                val isFavorite = checkIfFavorite(movieId)
                val isWatchlisted = checkIfWatchlisted(movieId)

                _uiState.value = _uiState.value.copy(
                    movieDetail = movieDetail,
                    isLoading = false,
                    isFavorite = isFavorite,
                    isWatchlisted = isWatchlisted
                )

                // Load similar movies in the background
                loadSimilarMovies(movieId)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load movie details: ${e.message}"
                )
            }
        }
    }

    fun loadSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingSimilar = true)

            try {
                delay(800)

                // In a real app: movieRepository.getSimilarMovies(movieId)
                val similarMovies = getSampleMovies().take(5) // Get first 5 as similar

                _uiState.value = _uiState.value.copy(
                    similarMovies = similarMovies,
                    isLoadingSimilar = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoadingSimilar = false)
                // Don't show error for similar movies, it's not critical
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentState = _uiState.value.isFavorite
            val movieId = _uiState.value.movieDetail?.id ?: return@launch

            try {
                // Optimistically update UI
                _uiState.value = _uiState.value.copy(isFavorite = !currentState)

                // In a real app: userRepository.toggleFavorite(movieId)
                delay(500) // Simulate API call

                // If API call fails, revert the state
                // _uiState.value = _uiState.value.copy(isFavorite = currentState)

            } catch (e: Exception) {
                // Revert on error
                _uiState.value = _uiState.value.copy(
                    isFavorite = currentState,
                    errorMessage = "Failed to update favorite status"
                )
            }
        }
    }

    fun toggleWatchlist() {
        viewModelScope.launch {
            val currentState = _uiState.value.isWatchlisted
            val movieId = _uiState.value.movieDetail?.id ?: return@launch

            try {
                // Optimistically update UI
                _uiState.value = _uiState.value.copy(isWatchlisted = !currentState)

                // In a real app: userRepository.toggleWatchlist(movieId)
                delay(500) // Simulate API call

            } catch (e: Exception) {
                // Revert on error
                _uiState.value = _uiState.value.copy(
                    isWatchlisted = currentState,
                    errorMessage = "Failed to update watchlist status"
                )
            }
        }
    }

    fun playTrailer() {
        viewModelScope.launch {
            try {
                val movieId = _uiState.value.movieDetail?.id ?: return@launch

                // In a real app, you would:
                // 1. Get trailer URL from API
                // 2. Open video player or YouTube

                println("Playing trailer for movie: $movieId")
                // For now, just simulate the action

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to play trailer: ${e.message}"
                )
            }
        }
    }

    fun shareMovie() {
        viewModelScope.launch {
            try {
                val movieDetail = _uiState.value.movieDetail ?: return@launch

                // In a real app, you would create a share intent
                val shareText = "Check out ${movieDetail.title}! ${movieDetail.homepage ?: ""}"

                println("Sharing: $shareText")
                // Implement actual sharing logic here

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to share movie"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun retry() {
        val movieId = _uiState.value.movieDetail?.id ?: return
        loadMovieDetail(movieId)
    }

    // Simulate checking user preferences
    private suspend fun checkIfFavorite(movieId: Int): Boolean {
        delay(200)
        // In a real app: return userRepository.isFavorite(movieId)
        return false // Default to false for sample
    }

    private suspend fun checkIfWatchlisted(movieId: Int): Boolean {
        delay(200)
        // In a real app: return userRepository.isWatchlisted(movieId)
        return false // Default to false for sample
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources if needed
    }
}