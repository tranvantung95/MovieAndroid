package com.example.feature.movies.presentation.model

// Enhanced data models for Movie Detail API response
data class MovieDetail(
    val adult: Boolean,
    val backdropPath: String?,
    val belongsToCollection: Collection?,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) {
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
        return genres.joinToString(", ") { it.name }
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
        return spokenLanguages.joinToString(", ") { it.englishName }
    }
}

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
) {
    fun getLogoUrl(size: String = "w154"): String? {
        return logoPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}

data class ProductionCountry(
    val iso31661: String,
    val name: String
)

data class SpokenLanguage(
    val englishName: String,
    val iso6391: String,
    val name: String
)

data class Collection(
    val id: Int,
    val name: String,
    val posterPath: String?,
    val backdropPath: String?
)