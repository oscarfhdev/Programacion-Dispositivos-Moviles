package com.example.gestorinventariotienda.data.remote

import retrofit2.http.GET

// Interfaz que define los endpoints (rutas) de la API que vamos a consumir.
interface FakeStoreApi {
    
    // Utilizamos un método GET para obtener la lista de productos predeterminada de FakeStore.
    // Usamos 'suspend' porque la llamada a la red debe hacerse en una corrutina en segundo plano.
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
