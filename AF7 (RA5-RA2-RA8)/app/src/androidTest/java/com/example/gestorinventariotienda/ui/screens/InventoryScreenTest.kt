package com.example.gestorinventariotienda.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.gestorinventariotienda.data.local.ProductEntity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class InventoryScreenTest {

    // Regla de orquestación pura para inflar vistas sin Actividad física
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productItem_displaysTitleAndQuantityLocally() {
        // 1. Objeto de prueba para nutrir a la Tarjeta State-less
        val testProduct = ProductEntity(
            id = 1,
            title = "Zapatillas Nike Air",
            price = 59.99,
            category = "Calzado",
            imageUrl = "",
            quantity = 15
        )

        // 2. Montamos el componente Node UI en el emulador virtual
        composeTestRule.setContent {
            ProductItem(
                product = testProduct,
                onStockUpdate = {},
                onDelete = {}
            )
        }

        // 3. Afirmaciones semánticas (Asserts) de la jerarquía UI
        // Verifica que el árbol semántico construyó e incluyó los Text(...)
        composeTestRule.onNodeWithText("Zapatillas Nike Air").assertIsDisplayed()
        composeTestRule.onNodeWithText("15").assertIsDisplayed()
    }
}
