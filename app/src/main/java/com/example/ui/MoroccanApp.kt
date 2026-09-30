package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CookingTimerBanner
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.RecipesScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.screens.VideoPlayerDialog
import com.example.ui.screens.VideosScreen
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaPrimary

@Composable
fun MoroccanApp(
    viewModel: RecipeViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val filteredRecipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedRecipe by viewModel.selectedRecipe.collectAsStateWithLifecycle()
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val currentServings by viewModel.currentServings.collectAsStateWithLifecycle()
    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    // Detail Screen (full overlay with BackHandler)
    if (selectedRecipe != null) {
        val recipe = selectedRecipe!!
        Box(modifier = Modifier.fillMaxSize()) {
            RecipeDetailScreen(
                recipe = recipe,
                currentServings = currentServings,
                isFavorite = favoriteIds.contains(recipe.id),
                onBack = { viewModel.closeRecipeDetail() },
                onServingsChange = { viewModel.updateServings(it) },
                onFavoriteToggle = { viewModel.toggleFavorite(recipe.id) },
                onAddToShoppingList = { viewModel.addRecipeToShoppingList(recipe) },
                onOpenVideoTutorial = { viewModel.openVideoTutorial(it) },
                onStartTimer = { duration, stepTitle -> viewModel.startTimer(duration, stepTitle) }
            )

            // Floating timer banner on top
            CookingTimerBanner(
                timerState = timerState,
                onTogglePause = { viewModel.toggleTimerPause() },
                onReset = { viewModel.resetTimer() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val uncompletedShoppingCount = shoppingItems.count { !it.isChecked }

                    // Tab 1: Recettes
                    NavigationBarItem(
                        selected = currentTab == AppTab.RECIPES,
                        onClick = { viewModel.selectTab(AppTab.RECIPES) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "Recettes"
                            )
                        },
                        label = { Text("Recettes", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            indicatorColor = TerracottaPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_recipes")
                    )

                    // Tab 2: Vidéos
                    NavigationBarItem(
                        selected = currentTab == AppTab.VIDEOS,
                        onClick = { viewModel.selectTab(AppTab.VIDEOS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = "Vidéos"
                            )
                        },
                        label = { Text("Vidéos", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            indicatorColor = TerracottaPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_videos")
                    )

                    // Tab 3: Courses
                    NavigationBarItem(
                        selected = currentTab == AppTab.SHOPPING,
                        onClick = { viewModel.selectTab(AppTab.SHOPPING) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (uncompletedShoppingCount > 0) {
                                        Badge(
                                            containerColor = SaffronAmber,
                                            contentColor = Color.White
                                        ) {
                                            Text(uncompletedShoppingCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Courses"
                                )
                            }
                        },
                        label = { Text("Courses", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            indicatorColor = TerracottaPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_shopping")
                    )

                    // Tab 4: Favoris
                    NavigationBarItem(
                        selected = currentTab == AppTab.FAVORITES,
                        onClick = { viewModel.selectTab(AppTab.FAVORITES) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favoris"
                            )
                        },
                        label = { Text("Favoris", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerracottaPrimary,
                            indicatorColor = TerracottaPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_tab_favorites")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.RECIPES -> RecipesScreen(
                        recipes = filteredRecipes,
                        favoriteIds = favoriteIds,
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.onSearchQueryChange(it) },
                        selectedCategory = selectedCategory,
                        onCategorySelect = { viewModel.selectCategory(it) },
                        onRecipeClick = { viewModel.openRecipeDetail(it) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it) }
                    )

                    AppTab.VIDEOS -> VideosScreen(
                        recipes = viewModel.allRecipes,
                        onSelectVideo = { viewModel.openVideoTutorial(it) }
                    )

                    AppTab.SHOPPING -> ShoppingListScreen(
                        items = shoppingItems,
                        onToggleItem = { id, checked -> viewModel.toggleShoppingItem(id, checked) },
                        onDeleteItem = { viewModel.deleteShoppingItem(it) },
                        onClearChecked = { viewModel.deleteCheckedShoppingItems() },
                        onClearAll = { viewModel.clearAllShoppingItems() },
                        onAddItem = { name, amount, cat -> viewModel.addCustomShoppingItem(name, amount, cat) }
                    )

                    AppTab.FAVORITES -> FavoritesScreen(
                        recipes = viewModel.allRecipes,
                        favoriteIds = favoriteIds,
                        onRecipeClick = { viewModel.openRecipeDetail(it) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it) }
                    )
                }

                // Floating timer banner on bottom above bottom nav
                CookingTimerBanner(
                    timerState = timerState,
                    onTogglePause = { viewModel.toggleTimerPause() },
                    onReset = { viewModel.resetTimer() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                )
            }
        }
    }

    // Video Player Modal
    if (activeVideo != null) {
        VideoPlayerDialog(
            video = activeVideo!!,
            onDismiss = { viewModel.closeVideoTutorial() }
        )
    }
}
