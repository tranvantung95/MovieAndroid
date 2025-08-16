package com.example.feature.movies.presentation

import android.graphics.drawable.ColorDrawable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.test.FakeImageLoaderEngine
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieTrendingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorDrawable(android.graphics.Color.GRAY))
            .build()

        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(engine)
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }

    @Test
    fun searchBar_displaysCorrectly() {
        composeTestRule.setContent {
            MaterialTheme {
                SearchBar(
                    query = "",
                    onQueryChange = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Search movies...")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Search")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_acceptsTextInput() {
        var capturedQuery = ""
        composeTestRule.setContent {
            MaterialTheme {
                SearchBar(
                    query = capturedQuery,
                    onQueryChange = { capturedQuery = it }
                )
            }
        }

        // Act - Type in search bar
        composeTestRule
            .onNodeWithText("Search movies...")
            .performTextInput("Batman")

        // Assert - Verify text was captured
        assertEquals("Batman", capturedQuery)
    }

    @Test
    fun movieTrendingScreen_displaysAllMovies() {
        val sampleMovies = getSampleMovies()

        composeTestRule.setContent {
            MaterialTheme {
                MovieTrendingScreen(
                    movieUis = sampleMovies,
                    isSearching = false,
                    onMovieClick = {}
                )
            }
        }

        sampleMovies.forEach { movie ->
            composeTestRule
                .onNodeWithText(movie.title)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun movieCard_displaysMovieInformation() {
        val sampleMovie = getSampleMovies().first()

        composeTestRule.setContent {
            MaterialTheme {
                MovieCard(
                    movieUi = sampleMovie,
                    onClick = {}
                )
            }
        }

        // Test main card exists
        composeTestRule
            .onNodeWithTag("movie_card_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        // Test specific elements
        composeTestRule
            .onNodeWithTag("movie_title_${sampleMovie.id}",  useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(sampleMovie.title)

        composeTestRule
            .onNodeWithTag("movie_year_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(sampleMovie.getReleaseYear())

        composeTestRule
            .onNodeWithTag("movie_overview_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(sampleMovie.overview)

        composeTestRule
            .onNodeWithTag("movie_rating_text_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("${sampleMovie.getFormattedRating()}/10")

        composeTestRule
            .onNodeWithTag("movie_vote_count_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("(${sampleMovie.voteCount})")

        composeTestRule
            .onNodeWithTag("movie_poster_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("movie_popularity_text_${sampleMovie.id}", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

}
