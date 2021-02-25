package com.challenge.omdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challenge.omdb.data.db.AppDatabase
import com.challenge.omdb.data.model.Movie
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the implementation of [MovieDaoTest]
 */
@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears after test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            // allowing main thread queries, just for testing
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getShoppingItemWhenNoItemInserted() {
        database.shoppingItemDao().getById(123L)
            .test()
            .assertNoValues()
    }

    @Test
    fun insertAndGetShoppingItem() {

        database.shoppingItemDao().insert(SHOPPPING_ITEM).blockingAwait()

        database.shoppingItemDao().getById(SHOPPPING_ITEM.shoppingItemId)
            .test()
            // assertValue asserts that there was only one emission of the item
            .assertValue { it.shoppingItemId == SHOPPPING_ITEM.shoppingItemId && it.name == SHOPPPING_ITEM.name }
    }

    @Test
    fun updateAndGetShoppingItem() {
        // Given that we have a item in the data source
        database.shoppingItemDao().insert(SHOPPPING_ITEM).blockingAwait()

        // When we are updating the name of the item
        val updatedShoppingItem =
            Movie(SHOPPPING_ITEM.shoppingItemId, SHOPPPING_ITEM.shoppingListId, "new item")
        database.shoppingItemDao()
            .update(updatedShoppingItem.shoppingItemId, updatedShoppingItem.name).blockingAwait()

        // When subscribing to the emissions of the item
        database.shoppingItemDao().getById(SHOPPPING_ITEM.shoppingItemId)
            .test()
            // assertValue asserts that there was only one emission of the item
            .assertValue { it.shoppingItemId == SHOPPPING_ITEM.shoppingItemId && it.name == "new item" }
    }

    @Test
    fun deleteAndGetShoppingItem() {
        // Given that we have a items in the data source
        database.shoppingItemDao().insert(SHOPPPING_ITEM).blockingAwait()

        //When we are deleting all items
        database.shoppingItemDao().deleteAll()
        // When subscribing to the emissions of the ite,
        database.shoppingItemDao().getById(SHOPPPING_ITEM.shoppingItemId)
            .test()
            // check that there's no item emitted
            .assertNoValues()
    }

    @Test
    fun getAllShoppingItemsFromShoppingList() {
        // Given that we have a items in assigned to shopping list with specified id
        database.shoppingItemDao().insert(SHOPPPING_ITEMS.first()).blockingAwait()
        database.shoppingItemDao().insert(SHOPPPING_ITEMS.last()).blockingAwait()

        //When we are fetching all items by shopping list id
        database.shoppingItemDao().getAllOrderByDate(SHOPPING_LIST_ID)
            .test()
            // Check that there are two items assigned to list
            .assertValue { it.count() == 2 }
    }

    companion object {
        private val SHOPPPING_ITEM = Movie(111, 33, "Apple")

        private const val SHOPPING_LIST_ID = 218738237L

        private val SHOPPPING_ITEMS = arrayOf(
            Movie(111, SHOPPING_LIST_ID, "Apple"),
            Movie(1234, SHOPPING_LIST_ID, "Bread")
        )
    }
}