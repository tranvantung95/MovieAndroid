package com.example.feature.movies.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core.feature.rememberNetworkConnection
import com.example.feature.movies.presentation.model.GenreUiModel
import com.example.feature.movies.presentation.model.MovieDetailUiModel
import com.example.feature.movies.presentation.model.ProductionCompanyUiModel
import com.example.feature.movies.presentation.model.ProductionCountryUiModel
import com.example.feature.movies.presentation.model.SpokenLanguageUiModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailRouter(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onPlayTrailerClick: () -> Unit,
    viewModel: MovieDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isNetworkAvailable = rememberNetworkConnection()
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage?.isNotEmpty() == true) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.errorMessage.orEmpty(),
                    actionLabel = "",
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearError()
        }
    }
    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }, topBar = {
        TopAppBar(navigationIcon = {
            IconButton(
                onClick = onBackClick, modifier = Modifier
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }, title = {
            Text(
                modifier = Modifier,
                maxLines = 1,
                text = state.movieDetail?.originalTitle.orEmpty(),
                style  = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        })

    }) { padding ->
        if (state.movieDetail != null) {
            MovieDetailScreen(
                modifier = modifier.padding(padding),
                movieDetail = state.movieDetail!!,
                onPlayTrailerClick = onPlayTrailerClick, isNetworkAvailable = isNetworkAvailable
            )
        } else if (!state.isLoading) {
            MovieDetailEmptyScreen(
                modifier = Modifier.padding(padding),
                isNetworkAvailable = isNetworkAvailable
            )
        }
    }

    AnimatedVisibility(visible = state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MovieDetailEmptyScreen(modifier: Modifier = Modifier, isNetworkAvailable: Boolean) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NetworkWarningBanner(isVisible = !isNetworkAvailable, modifier = Modifier.fillMaxWidth())
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nothing to display",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetailUiModel,
    isNetworkAvailable: Boolean = true,
    onPlayTrailerClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        NetworkWarningBanner(isVisible = !isNetworkAvailable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = movieDetail.getBackdropUrl(),
                contentDescription = "${movieDetail.title} backdrop",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                                MaterialTheme.colorScheme.background
                            ), startY = 0f, endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Poster
            AsyncImage(
                model = movieDetail.getPosterUrl(),
                contentDescription = "${movieDetail.title} poster",
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // Movie Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movieDetail.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Original Title (if different)
                if (movieDetail.originalTitle != movieDetail.title) {
                    Text(
                        text = movieDetail.originalTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }

                // Tagline
                movieDetail.tagline?.let { tagline ->
                    if (tagline.isNotEmpty()) {
                        Text(
                            text = "\"$tagline\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movieDetail.getFormattedRating()}/10",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${movieDetail.voteCount} votes)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Play Trailer Button
                Button(
                    onClick = onPlayTrailerClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow, contentDescription = "Play"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play Trailer")
                }
            }
        }

        MovieStatsSection(movieDetail = movieDetail)
        GenresSection(genreUiModels = movieDetail.genreUiModels)
        OverviewSection(overview = movieDetail.overview)
        ProductionCompaniesSection(companies = movieDetail.productionCompanies)
        AdditionalInfoSection(movieDetail = movieDetail)
        Spacer(modifier = Modifier.height(32.dp))

    }
}

@Composable
fun MovieStatsSection(movieDetail: MovieDetailUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Default.DateRange,
                label = "Release",
                value = movieDetail.getReleaseYear()
            )
            StatItem(
                icon = Icons.Default.AccessTime,
                label = "Runtime",
                value = movieDetail.getFormattedRuntime()
            )
            StatItem(
                icon = Icons.Default.Language,
                label = "Language",
                value = movieDetail.originalLanguage.uppercase()
            )
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector, label: String, value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GenresSection(genreUiModels: List<GenreUiModel>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Genres",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genreUiModels) { genre ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = genre.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OverviewSection(overview: String) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = overview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun ProductionCompaniesSection(companies: List<ProductionCompanyUiModel>) {
    if (companies.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Production Companies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(companies) { company ->
                    ProductionCompanyItem(company = company)
                }
            }
        }
    }
}

@Composable
fun ProductionCompanyItem(company: ProductionCompanyUiModel) {
    Card(
        modifier = Modifier.width(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (company.logoPath != null) {
                AsyncImage(
                    model = company.getLogoUrl(),
                    contentDescription = "${company.name} logo",
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = company.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AdditionalInfoSection(movieDetail: MovieDetailUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoRow(label = "Status", value = movieDetail.status)
            InfoRow(label = "Budget", value = movieDetail.getFormattedBudget())
            InfoRow(label = "Revenue", value = movieDetail.getFormattedRevenue())
            InfoRow(
                label = "Production Countries", value = movieDetail.getProductionCountriesString()
            )
            InfoRow(label = "Spoken Languages", value = movieDetail.getSpokenLanguagesString())

            movieDetail.homepage?.let { homepage ->
                if (homepage.isNotEmpty()) {
                    InfoRow(label = "Homepage", value = homepage)
                }
            }

            movieDetail.imdbId?.let { imdbId ->
                InfoRow(label = "IMDB ID", value = imdbId)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

// Sample data function for testing
fun getSampleMovieDetail(): MovieDetailUiModel {
    return MovieDetailUiModel(
        adult = false,
        backdropPath = "/y1LSnnLu4fPMHDEv0FbybDKtxWD.jpg",
        belongsToCollectionUiModel = null,
        budget = 25000000,
        genreUiModels = listOf(
            GenreUiModel(37, "Western"), GenreUiModel(35, "Comedy"), GenreUiModel(80, "Crime")
        ),
        homepage = "https://a24films.com/films/eddington",
        id = 648878,
        imdbId = "tt31176520",
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalTitle = "Eddington",
        overview = "In May of 2020, a standoff between a small-town sheriff and mayor sparks a powder keg as neighbor is pitted against neighbor in Eddington, New Mexico.",
        popularity = 7.8876,
        posterPath = "/4GIqZUgPZ146BhibsPHMHef2nXX.jpg",
        productionCompanies = listOf(
            ProductionCompanyUiModel(41077, "/1ZXsGaFPgrgS6ZZGS37AqD5uU12.png", "A24", "US"),
            ProductionCompanyUiModel(
                123620, "/ePRhZ3yb09Ya6WMzCCBYopwIYbE.png", "Square Peg", "US"
            ),
            ProductionCompanyUiModel(178359, null, "828 Productions", "US")
        ),
        productionCountries = listOf(
            ProductionCountryUiModel("US", "United States of America"),
            ProductionCountryUiModel("FI", "Finland")
        ),
        releaseDate = "2025-07-16",
        revenue = 11299038,
        runtime = 149,
        spokenLanguageUiModels = listOf(
            SpokenLanguageUiModel("English", "en", "English")
        ),
        status = "Released",
        tagline = "Hindsight is 2020.",
        title = "Eddington",
        video = false,
        voteAverage = 6.6,
        voteCount = 87
    )
}

@Composable
@Preview(showBackground = true)
fun MovieDetailScreenPreview() {
    MovieDetailScreen(movieDetail = getSampleMovieDetail())
}

@Composable
@Preview(showBackground = true)
fun MovieDetailEmptyScreenPreview() {
    MovieDetailEmptyScreen(isNetworkAvailable = false)
}