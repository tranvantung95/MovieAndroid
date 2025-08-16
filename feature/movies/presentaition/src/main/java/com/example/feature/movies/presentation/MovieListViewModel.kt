package com.example.feature.movies.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.movies.presentation.mapper.MovieUiMapper
import com.example.share.feature.movie.domain.GetMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MovieListViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val movieUiMapper: MovieUiMapper
) : ViewModel() {
    private var movieQuery by mutableStateOf("")
    private val queryFlow = snapshotFlow { movieQuery }
        .distinctUntilChanged()

    private val _uiState = MutableStateFlow(MovieListUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val apiResultsFlow = queryFlow
        .flatMapLatest { query ->
            getMoviesUseCase.invoke(query)
                .map { result ->
                    result.fold(
                        onSuccess = { movies ->
                            ApiState.Success(movies)
                        },
                        onFailure = { error ->
                            ApiState.Error(error.message ?: "Unknown error")
                        }
                    )
                }.onStart {
                    emit(ApiState.Loading)
                }
        }
        .catch { exception ->
            emit(ApiState.Error(exception.message ?: "Unknown error"))
        }

    val uiState: StateFlow<MovieListUiState> = combine(
        _uiState,
        queryFlow,
        apiResultsFlow
    ) { currentState, _, apiResult ->
        when (apiResult) {
            is ApiState.Loading -> currentState.copy(
                isLoading = true,
                movieUis = emptyList(),
                errorMessage = null
            )

            is ApiState.Success -> currentState.copy(
                isLoading = false,
                movieUis = movieUiMapper.mapToUIList(apiResult.data),
                errorMessage = null
            )

            is ApiState.Error -> currentState.copy(
                isLoading = false,
                movieUis = emptyList(),
                errorMessage = apiResult.message
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieListUiState()
    )

    fun onSearchQueryChanged(query: String) {
        movieQuery = query
        _uiState.update { it.copy(searchQuery = query, isLoading = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

sealed class ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
}