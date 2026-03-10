package com.example.gestorinventariotienda.data.remote

import com.google.gson.annotations.SerializedName

// Data Transfer Object (DTO) para mapear el JSON que recibimos de FakeStore API.
// Esto nos sirve de intermediario antes de guardarlo en nuestra base de datos local (Room).
data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String
)
