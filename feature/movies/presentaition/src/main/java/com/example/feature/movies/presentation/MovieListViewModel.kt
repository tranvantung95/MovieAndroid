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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    val mapper = MovieUiMapper()
    private var movieQuery by mutableStateOf("")
    private val queryFlow = snapshotFlow { movieQuery }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val apiResultsFlow = queryFlow
        .flatMapLatest { query ->
            getMoviesUseCase.invoke(query)
                .map { result ->
                    result.fold(
                        onSuccess = { movies ->
                            ApiState.Success(movies)
                        },
                        onFailure = { error -> ApiState.Error(error.message ?: "Unknown error") }
                    )
                }
                .onStart {
                    emit(ApiState.Loading)
                }
        }
        .catch { exception ->
            emit(ApiState.Error(exception.message ?: "Unknown error"))
        }

    val uiState: StateFlow<MovieListUiState> = combine(
        queryFlow,
        apiResultsFlow
    ) { query, apiResult ->
        when (apiResult) {
            is ApiState.Loading -> MovieListUiState(
                searchQuery = query,
                isLoading = true,
                movieUis = emptyList(),
                errorMessage = null
            )

            is ApiState.Success -> MovieListUiState(
                searchQuery = query,
                isLoading = false,
                movieUis = mapper.mapToUIList(apiResult.data),
                errorMessage = null
            )

            is ApiState.Error -> MovieListUiState(
                searchQuery = query,
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
    }
}

sealed class ApiState<out T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
}