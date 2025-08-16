package com.example.feature.movies.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.feature.movies.presentation.fakedomain.GetMovieDetailUseCase
import com.example.feature.movies.presentation.navigation.MovieDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {
    private val destination = savedStateHandle.toRoute<MovieDetailDestination>()
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    private val getDetailFlow = flow {
        emit(ApiState.Loading)
        getMovieDetailUseCase.invoke(destination.id).onSuccess {
            emit(ApiState.Success(it))
        }.onFailure {
            emit(ApiState.Error(it.message.orEmpty()))
        }
    }.catch { emit(ApiState.Error(it.message.orEmpty())) }
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
                    currentState.copy(isLoading = false, movieDetail = movies.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieDetailUiState()
        )
    fun clearError(){
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}