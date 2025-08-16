package com.example.feature.movies.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.feature.movies.presentation.mapper.MovieDetailMapper
import com.example.feature.movies.presentation.navigation.MovieDetailDestination
import com.example.share.feature.movie.domain.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MovieDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val movieDetailMapper: MovieDetailMapper
) : ViewModel() {
    private val destination = savedStateHandle.toRoute<MovieDetailDestination>()
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    private val getDetailFlow =
        getMovieDetailUseCase.invoke(destination.id).map { response ->
            response.fold(onSuccess = {
                ApiState.Success(it)
            }, onFailure = {
                ApiState.Error(it.message.orEmpty())
            })
        }.onStart {
            emit(ApiState.Loading)
        }.catch {
            emit(ApiState.Error(it.message.orEmpty()))
        }

    val uiState: StateFlow<MovieDetailUiState> =
        combine(getDetailFlow, _uiState) { movies, currentState ->
            when (movies) {
                is ApiState.Loading -> {
                    currentState.copy(isLoading = true)
                }

                is ApiState.Error -> {
                    currentState.copy(isLoading = false, errorMessage = movies.message)
                }

                is ApiState.Success -> {
                    currentState.copy(
                        isLoading = false,
                        movieDetail = movies.data?.let {
                            movieDetailMapper.mapToUi(it)
                        } ?: kotlin.run { null }
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieDetailUiState()
        )

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}