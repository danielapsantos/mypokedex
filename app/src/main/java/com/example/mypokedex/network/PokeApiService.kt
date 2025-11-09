package com.example.mypokedex.network

import com.example.mypokedex.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET

interface PokeApiService {
    @GET("pokemon?limit=151")
    fun getPokemons(): Call<PokemonResponse>
}
