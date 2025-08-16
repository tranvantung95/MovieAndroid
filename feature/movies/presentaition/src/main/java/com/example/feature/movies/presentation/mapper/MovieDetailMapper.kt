package com.example.feature.movies.presentation.mapper

import com.example.feature.movies.presentation.core.DomainToUiModel
import com.example.feature.movies.presentation.model.CollectionUiModel
import com.example.feature.movies.presentation.model.GenreUiModel
import com.example.feature.movies.presentation.model.MovieDetailUiModel
import com.example.feature.movies.presentation.model.ProductionCompanyUiModel
import com.example.feature.movies.presentation.model.ProductionCountryUiModel
import com.example.feature.movies.presentation.model.SpokenLanguageUiModel
import com.example.share.feature.movie.domain.model.MovieDetail

class MovieDetailMapper : DomainToUiModel<MovieDetailUiModel, MovieDetail> {
    override fun mapToUi(domain: MovieDetail): MovieDetailUiModel {
        return MovieDetailUiModel(
            adult = domain.adult,
            backdropPath = domain.backdropPath,
            belongsToCollectionUiModel = CollectionUiModel(
                id = domain.belongsToCollection?.id,
                name = domain.belongsToCollection?.name.orEmpty(),
                posterPath = domain.belongsToCollection?.posterPath,
                backdropPath = domain.belongsToCollection?.backdropPath
            ),
            budget = domain.budget,
            genreUiModels = domain.genres.map {
                GenreUiModel(
                    id = it.id,
                    name = it.name
                )
            },
            homepage = domain.homepage,
            id = domain.id,
            imdbId = domain.imdbId,
            originCountry = domain.originCountry,
            originalLanguage = domain.originalLanguage,
            originalTitle = domain.originalTitle,
            overview = domain.overview,
            popularity = domain.popularity,
            posterPath = domain.posterPath,
            productionCompanies = domain.productionCompanies.map {
                ProductionCompanyUiModel(
                    id = it.id,
                    name = it.name,
                    logoPath = it.logoPath,
                    originCountry = it.originCountry
                )
            },
            productionCountries = domain.productionCountries.map {
                ProductionCountryUiModel(
                    iso31661 = it.iso31661,
                    name = it.name
                )
            },
            releaseDate = domain.releaseDate,
            revenue = domain.revenue,
            runtime = domain.runtime,
            spokenLanguageUiModels = domain.spokenLanguages.map {
                SpokenLanguageUiModel(
                    iso6391 = it.iso6391,
                    englishName = it.englishName,
                    name = it.name
                )
            },
            status = domain.status,
            tagline = domain.tagline,
            title = domain.title,
            video = domain.video,
            voteAverage = domain.voteAverage,
            voteCount = domain.voteCount
        )
    }
}