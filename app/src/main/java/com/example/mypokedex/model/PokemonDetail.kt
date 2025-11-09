package com.example.mypokedex.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStatSlot>
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

data class PokemonType(
    val name: String
)

data class PokemonStatSlot(
    val base_stat: Int,
    val stat: PokemonStat
)

data class PokemonStat(
    val name: String
)