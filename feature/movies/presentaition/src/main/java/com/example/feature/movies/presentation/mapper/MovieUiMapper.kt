package com.example.feature.movies.presentation.mapper

import com.example.feature.movies.presentation.core.DomainToUiModel
import com.example.feature.movies.presentation.model.MovieUi
import com.example.share.feature.movie.domain.model.Movie

class MovieUiMapper : DomainToUiModel<MovieUi, Movie> {
    override fun mapToUi(domain: Movie): MovieUi {
        return MovieUi(
            id = domain.id,
            title = domain.title,
            originalTitle = domain.originalTitle,
            overview = domain.overview,
            posterPath = domain.posterPath,
            backdropPath = domain.backdropPath,
            releaseDate = domain.releaseDate,
            voteAverage = domain.voteAverage,
            voteCount = domain.voteCount,
            popularity = domain.popularity,
            adult = domain.adult,
            originalLanguage = domain.originalLanguage,
            genreIds = domain.genreIds,
            video = domain.video
        )
    }

}