package com.example.mypokedex.model

data class Pokemon(
    val name: String,
    val imageUrl: String,
    val id: String,
    val types: List<String> = emptyList()
)