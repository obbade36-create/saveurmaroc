package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ShoppingItemEntity
import com.example.data.model.Recipe
import com.example.data.model.RecipeCategory
import com.example.data.model.VideoTutorial
import com.example.data.repository.MoroccanDataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String, val iconName: String) {
    RECIPES("Recettes", "restaurant_menu"),
    VIDEOS("Vidéos", "smart_display"),
    SHOPPING("Courses", "shopping_cart"),
    FAVORITES("Favoris & Épices", "star")
}

data class TimerState(
    val remainingSeconds: Int = 0,
    val totalSeconds: Int = 0,
    val isRunning: Boolean = false,
    val stepTitle: String = ""
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = MoroccanDataRepository(db.shoppingDao(), db.favoritesDao())

    val allRecipes: List<Recipe> = repository.getRecipes()

    // Navigation & Tab state
    private val _currentTab = MutableStateFlow(AppTab.RECIPES)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Search & Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(RecipeCategory.ALL)
    val selectedCategory: StateFlow<RecipeCategory> = _selectedCategory.asStateFlow()

    // Active recipe details & video
    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _activeVideo = MutableStateFlow<VideoTutorial?>(null)
    val activeVideo: StateFlow<VideoTutorial?> = _activeVideo.asStateFlow()

    // Servings multiplier (default 4 servings)
    private val _currentServings = MutableStateFlow(4)
    val currentServings: StateFlow<Int> = _currentServings.asStateFlow()

    // Timer state
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    private var timerJob: Job? = null

    // Notification message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Room Flows
    val shoppingItems: StateFlow<List<ShoppingItemEntity>> = repository.allShoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<Set<String>> = repository.allFavorites
        .map { list -> list.map { it.recipeId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Filtered recipes
    val filteredRecipes: StateFlow<List<Recipe>> = combine(
        _searchQuery,
        _selectedCategory,
        favoriteIds
    ) { query, category, _ ->
        allRecipes.filter { recipe ->
            val matchesCategory = category == RecipeCategory.ALL || recipe.category == category
            val matchesQuery = query.isBlank() ||
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.arabicTitle.contains(query, ignoreCase = true) ||
                recipe.description.contains(query, ignoreCase = true) ||
                recipe.ingredients.any { it.name.contains(query, ignoreCase = true) } ||
                recipe.keySpices.any { it.contains(query, ignoreCase = true) }

            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allRecipes)

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: RecipeCategory) {
        _selectedCategory.value = category
    }

    fun openRecipeDetail(recipe: Recipe) {
        _selectedRecipe.value = recipe
        _currentServings.value = recipe.defaultServings
    }

    fun closeRecipeDetail() {
        _selectedRecipe.value = null
    }

    fun openVideoTutorial(video: VideoTutorial) {
        _activeVideo.value = video
    }

    fun closeVideoTutorial() {
        _activeVideo.value = null
    }

    fun updateServings(newServings: Int) {
        if (newServings in 1..16) {
            _currentServings.value = newServings
        }
    }

    fun toggleFavorite(recipeId: String) {
        val isFav = favoriteIds.value.contains(recipeId)
        viewModelScope.launch {
            repository.toggleFavorite(recipeId, isFav)
            _userMessage.value = if (!isFav) "Ajouté aux favoris ❤️" else "Retiré des favoris"
        }
    }

    fun addRecipeToShoppingList(recipe: Recipe) {
        viewModelScope.launch {
            val multiplier = _currentServings.value.toDouble() / recipe.defaultServings.toDouble()
            repository.addRecipeIngredientsToShopping(recipe, multiplier)
            _userMessage.value = "${recipe.ingredients.size} ingrédients ajoutés à la liste de courses !"
        }
    }

    fun addCustomShoppingItem(name: String, amount: String, category: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addShoppingItem(
                ShoppingItemEntity(
                    name = name.trim(),
                    amount = amount.trim(),
                    category = category,
                    isChecked = false,
                    recipeTitle = "Ajout manuel"
                )
            )
            _userMessage.value = "Article ajouté à votre liste"
        }
    }

    fun toggleShoppingItem(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            repository.toggleShoppingItem(id, isChecked)
        }
    }

    fun deleteShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun deleteCheckedShoppingItems() {
        viewModelScope.launch {
            repository.deleteCheckedItems()
            _userMessage.value = "Articles cochés supprimés"
        }
    }

    fun clearAllShoppingItems() {
        viewModelScope.launch {
            repository.clearShoppingList()
            _userMessage.value = "Liste de courses réinitialisée"
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // Timer controls
    fun startTimer(minutes: Int, stepTitle: String) {
        val totalSec = minutes * 60
        _timerState.value = TimerState(
            remainingSeconds = totalSec,
            totalSeconds = totalSec,
            isRunning = true,
            stepTitle = stepTitle
        )
        runTimer()
    }

    fun toggleTimerPause() {
        val current = _timerState.value
        if (current.isRunning) {
            timerJob?.cancel()
            _timerState.value = current.copy(isRunning = false)
        } else if (current.remainingSeconds > 0) {
            _timerState.value = current.copy(isRunning = true)
            runTimer()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState()
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000)
                val currentRem = _timerState.value.remainingSeconds - 1
                if (currentRem <= 0) {
                    _timerState.value = _timerState.value.copy(remainingSeconds = 0, isRunning = false)
                    _userMessage.value = "⏰ Temps écoulé : ${_timerState.value.stepTitle} !"
                    break
                } else {
                    _timerState.value = _timerState.value.copy(remainingSeconds = currentRem)
                }
            }
        }
    }
}
