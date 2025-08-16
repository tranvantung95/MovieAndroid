package com.example.feature.movies.presentation.fakedomain

//
//interface GetMoviesUseCase {
//    suspend operator fun invoke(searchQuery: String): Result<List<Movie>>
//}

//class GetMoviesUseCaseImpl @Inject constructor(
//    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
//    private val searchMoviesUseCase: SearchMoviesUseCase
//) : GetMoviesUseCase {
//
//    override suspend fun invoke(searchQuery: String): Result<List<Movie>> {
//        return if (searchQuery.isEmpty()) {
//            // Call /trending API
//            getTrendingMoviesUseCase()
//        } else {
//            // Call /search API
//            searchMoviesUseCase(searchQuery)
//        }
//    }
//}