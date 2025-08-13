package com.example.feature.movies.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.movies.presentation.fakedomain.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieListUiState())

    var movieQuery by mutableStateOf("")
        private set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val uiState: StateFlow<MovieListUiState> = snapshotFlow {
        movieQuery
    }.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            println("movie_router flatmap $query")
            flow {
                emit(
                    MovieListUiState(
                        searchQuery = query,
                        isLoading = true
                    )
                )
                getMoviesUseCase.invoke(query)
                    .onSuccess { movies ->
                        emit(
                            MovieListUiState(
                                searchQuery = query,
                                movies = movies,
                                isLoading = false
                            )
                        )
                    }
                    .onFailure { exception ->
                        emit(
                            MovieListUiState(
                                searchQuery = query,
                                isLoading = false,
                                errorMessage = exception.message ?: "Unknown Error"
                            )
                        )
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieListUiState(searchQuery = "")
        )

    fun onSearchQueryChanged(query: String) {
        println("movie_router,, viewmodel $query")
        movieQuery = query
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

}

sealed class ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
}