package com.example.gestorinventariotienda.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Base de datos en memoria; los datos se pierden al cerrar el contexto. Ideal para CI/CD.
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        productDao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadProduct() = runTest {
        val product = ProductEntity(
            id = 1,
            title = "Zapatillas de Test",
            price = 45.5,
            category = "Zapatos",
            imageUrl = "",
            quantity = 20
        )

        // Ejecutar Insert DAO
        productDao.insertProducts(listOf(product))

        // Evaluar Flow DAO
        val allProducts = productDao.getAllProducts().first()

        // Pruebas unitarias rígidas (Asserts)
        assertEquals(1, allProducts.size)
        assertEquals("Zapatillas de Test", allProducts[0].title)
        assertEquals(20, allProducts[0].quantity)
    }
}
