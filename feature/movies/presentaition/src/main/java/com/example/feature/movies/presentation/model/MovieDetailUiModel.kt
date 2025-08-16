package com.example.feature.movies.presentation.model

import com.example.core.feature.UiModel

// Enhanced data models for Movie Detail API response
data class MovieDetailUiModel(
    val adult: Boolean,
    val backdropPath: String?,
    val belongsToCollectionUiModel: CollectionUiModel?,
    val budget: Long,
    val genreUiModels: List<GenreUiModel>,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompanyUiModel>,
    val productionCountries: List<ProductionCountryUiModel>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguageUiModels: List<SpokenLanguageUiModel>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) : UiModel {
    // Helper function to get full poster URL
    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    // Helper function to get full backdrop URL
    fun getBackdropUrl(size: String = "w1280"): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    // Helper function to format release year
    fun getReleaseYear(): String {
        return if (releaseDate.isNotEmpty()) {
            releaseDate.substring(0, 4)
        } else {
            "Unknown"
        }
    }

    // Helper function to format rating
    fun getFormattedRating(): String {
        return String.format("%.1f", voteAverage)
    }

    // Helper function to format runtime
    fun getFormattedRuntime(): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }

    // Helper function to format budget
    fun getFormattedBudget(): String {
        return if (budget > 0) {
            "$${String.format("%,d", budget)}"
        } else {
            "Not disclosed"
        }
    }

    // Helper function to format revenue
    fun getFormattedRevenue(): String {
        return if (revenue > 0) {
            "$${String.format("%,d", revenue)}"
        } else {
            "Not disclosed"
        }
    }

    // Helper function to get genres as string
    fun getGenresString(): String {
        return genreUiModels.joinToString(", ") { it.name }
    }

    // Helper function to get production companies as string
    fun getProductionCompaniesString(): String {
        return productionCompanies.joinToString(", ") { it.name }
    }

    // Helper function to get production countries as string
    fun getProductionCountriesString(): String {
        return productionCountries.joinToString(", ") { it.name }
    }

    // Helper function to get spoken languages as string
    fun getSpokenLanguagesString(): String {
        return spokenLanguageUiModels.joinToString(", ") { it.englishName }
    }
}

data class GenreUiModel(
    val id: Int,
    val name: String
)

data class ProductionCompanyUiModel(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
) {
    fun getLogoUrl(size: String = "w154"): String? {
        return logoPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}

data class ProductionCountryUiModel(
    val iso31661: String,
    val name: String
)

data class SpokenLanguageUiModel(
    val englishName: String,
    val iso6391: String,
    val name: String
): UiModel

data class CollectionUiModel(
    val id: Int? = null,
    val name: String,
    val posterPath: String?,
    val backdropPath: String?
): UiModel