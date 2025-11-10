package com.example.mypokedex.network
import com.example.mypokedex.model.PokemonDetail
import com.example.mypokedex.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon?limit=151")
    suspend fun getPokemons(): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetail
}
