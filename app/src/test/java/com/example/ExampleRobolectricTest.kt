package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.FavoriteRecipeEntity
import com.example.data.local.FavoritesDao
import com.example.data.local.ShoppingDao
import com.example.data.local.ShoppingItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var shoppingDao: ShoppingDao
    private lateinit var favoritesDao: FavoritesDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        shoppingDao = database.shoppingDao()
        favoritesDao = database.favoritesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Saveurs du Maroc", appName)
    }

    @Test
    fun `persist and retrieve shopping list items in Room`() = runBlocking {
        val item1 = ShoppingItemEntity(
            name = "Ras el Hanout",
            amount = "2 c. à soupe",
            category = "Épices & Aromates",
            isChecked = false,
            recipeTitle = "Tajine d'Agneau"
        )
        val item2 = ShoppingItemEntity(
            name = "Citrons confits Beldi",
            amount = "2 pièces",
            category = "Épicerie fine",
            isChecked = true,
            recipeTitle = "Tajine de Poulet"
        )

        val id1 = shoppingDao.insertItem(item1)
        shoppingDao.insertItem(item2)

        val items = shoppingDao.getAllShoppingItems().first()
        assertEquals(2, items.size)

        // Toggle check status
        shoppingDao.updateCheckedStatus(id1, true)
        val updatedItems = shoppingDao.getAllShoppingItems().first()
        val updatedItem1 = updatedItems.first { it.name == "Ras el Hanout" }
        assertTrue(updatedItem1.isChecked)

        // Delete checked
        shoppingDao.deleteCheckedItems()
        val remaining = shoppingDao.getAllShoppingItems().first()
        assertEquals(0, remaining.size)
    }

    @Test
    fun `persist and toggle favorite recipes in Room`() = runBlocking {
        val recipeId = "tajine-agneau-pruneaux"

        // Initially not favorite
        val initialFav = favoritesDao.isFavorite(recipeId).first()
        assertFalse(initialFav)

        // Insert favorite
        favoritesDao.addFavorite(FavoriteRecipeEntity(recipeId = recipeId))
        val isFav = favoritesDao.isFavorite(recipeId).first()
        assertTrue(isFav)

        val allFavs = favoritesDao.getAllFavorites().first()
        assertEquals(1, allFavs.size)
        assertEquals(recipeId, allFavs[0].recipeId)

        // Remove favorite
        favoritesDao.removeFavorite(recipeId)
        val afterRemoval = favoritesDao.isFavorite(recipeId).first()
        assertFalse(afterRemoval)
    }
}
