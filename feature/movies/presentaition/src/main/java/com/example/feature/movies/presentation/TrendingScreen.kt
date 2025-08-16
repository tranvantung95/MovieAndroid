package com.example.feature.movies.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.feature.movies.presentation.mapper.MovieUiMapper
import com.example.feature.movies.presentation.model.MovieUi

@Composable
fun MovieRouter(
    modifier: Modifier = Modifier,
    onMovieClick: (MovieUi) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(modifier = modifier, topBar = {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            query = uiState.searchQuery,
            onQueryChange = {
                viewModel.onSearchQueryChanged(it)
            })
    }) { paddingValues ->
        MovieTrendingScreen(
            modifier = Modifier.padding(paddingValues),
            movieUis = uiState.movieUis,
            onMovieClick = onMovieClick,
            isSearching = uiState.searchQuery.isNotEmpty()
        )
        AnimatedVisibility(visible = uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun MovieTrendingScreen(
    modifier: Modifier = Modifier,
    movieUis: List<MovieUi>,
    isSearching: Boolean = false,
    onMovieClick: (MovieUi) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = if (isSearching) "Search result" else "Trending movies",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Movies List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movieUis) { movie ->
                MovieCard(
                    movieUi = movie,
                    onClick = { onMovieClick(movie) }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search movies...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        singleLine = true
    )
}

@Composable
fun MovieCard(
    movieUi: MovieUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Movie Poster
            AsyncImage(
                model = movieUi.getPosterUrl() ?: movieUi.getBackdropUrl(),
                contentDescription = "${movieUi.title} poster",
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Movie Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Title
                Text(
                    text = movieUi.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Release Year
                Text(
                    text = movieUi.getReleaseYear(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700), // Gold color
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movieUi.getFormattedRating()}/10",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${movieUi.voteCount})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Overview
                Text(
                    text = movieUi.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )

                // Popularity Badge
                Surface(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Popularity: ${String.format("%.0f", movieUi.popularity)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MovieCardPreview() {
    MovieCard(modifier = Modifier, movieUi = getSampleMovies().first(), onClick = {

    })
}

@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    SearchBar(modifier = Modifier, query = "", onQueryChange = {

    })
}

// Sample data function using your provided JSON
fun getSampleMovies(): List<MovieUi> {
    return listOf(
        MovieUi(
            id = 648878,
            title = "Eddington",
            originalTitle = "Eddington",
            overview = "In May of 2020, a standoff between a small-town sheriff and mayor sparks a powder keg as neighbor is pitted against neighbor in Eddington, New Mexico.",
            posterPath = "/4GIqZUgPZ146BhibsPHMHef2nXX.jpg",
            backdropPath = "/5PJkK2iVXRO1ydrtgwuJdevmnOe.jpg",
            releaseDate = "2025-07-16",
            voteAverage = 6.6,
            voteCount = 90,
            popularity = 49.6073,
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(37, 35, 80),
            video = false
        ),
        MovieUi(
            id = 936108,
            title = "Smurfs",
            originalTitle = "Smurfs",
            overview = "When Papa Smurf is mysteriously taken by evil wizards, Razamel and Gargamel, Smurfette leads the Smurfs on a mission into the real world to save him. With the help of new friends, the Smurfs must discover what defines their destiny to save the universe.",
            posterPath = "/8o6lkhL32xQJeB52IIG1us5BVey.jpg",
            backdropPath = "/9whEVuKte4Qi0LI4TzPf7glinJW.jpg",
            releaseDate = "2025-07-05",
            voteAverage = 5.849,
            voteCount = 64,
            popularity = 47.0877,
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(16, 10751, 14),
            video = false
        ),
        MovieUi(
            id = 1078605,
            title = "Weapons",
            originalTitle = "Weapons",
            overview = "When all but one child from the same class mysteriously vanish on the same night at exactly the same time, a community is left questioning who or what is behind their disappearance.",
            posterPath = "/cpf7vsRZ0MYRQcnLWteD5jK9ymT.jpg",
            backdropPath = "/Q2OajDi2kcO6yErb1IAyVDTKMs.jpg",
            releaseDate = "2025-08-04",
            voteAverage = 7.7,
            voteCount = 261,
            popularity = 266.8042,
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(27, 9648),
            video = false
        ),
        MovieUi(
            id = 1234821,
            title = "Jurassic World Rebirth",
            originalTitle = "Jurassic World Rebirth",
            overview = "Five years after the events of Jurassic World Dominion, covert operations expert Zora Bennett is contracted to lead a skilled team on a top-secret mission to secure genetic material from the world's three most massive dinosaurs. When Zora's operation intersects with a civilian family whose boating expedition was capsized, they all find themselves stranded on an island where they come face-to-face with a sinister, shocking discovery that's been hidden from the world for decades.",
            posterPath = "/1RICxzeoNCAO5NpcRMIgg1XT6fm.jpg",
            backdropPath = "/zNriRTr0kWwyaXPzdg1EIxf0BWk.jpg",
            releaseDate = "2025-07-01",
            voteAverage = 6.406,
            voteCount = 1440,
            popularity = 1090.5346,
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(878, 12, 28),
            video = false
        ),
        MovieUi(
            id = 986056,
            title = "Thunderbolts*",
            originalTitle = "Thunderbolts*",
            overview = "After finding themselves ensnared in a death trap, seven disillusioned castoffs must embark on a dangerous mission that will force them to confront the darkest corners of their pasts.",
            posterPath = "/hqcexYHbiTBfDIdDWxrxPtVndBX.jpg",
            backdropPath = "/rthMuZfFv4fqEU4JVbgSW9wQ8rs.jpg",
            releaseDate = "2025-04-30",
            voteAverage = 7.402,
            voteCount = 2179,
            popularity = 97.3721,
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(28, 878, 12),
            video = false
        )
    )
}