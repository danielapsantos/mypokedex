package com.example.mypokedex

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.model.Pokemon
import com.example.mypokedex.model.PokemonResponse
import com.example.mypokedex.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView
    private var allPokemons = listOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchPokemons()
        setupSearch()
    }
    private fun fetchPokemons() {
        RetrofitInstance.api.getPokemons().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(
                call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.results ?: emptyList()

                    allPokemons = results.map { result ->
                        val id = result.url.split("/").dropLast(1).last()
                        val imageUrl =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                        Log.d("PokedexApp", "Criando Pokémon: ${result.name} com ID: $id")
                        Pokemon(result.name, imageUrl)
                    }
                    Log.d("PokedexApp", "Lista final pronta. Tamanho: ${allPokemons.size}")
                    adapter.updateList(allPokemons)
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    private fun setupSearch() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = allPokemons.filter {
                    it.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateList(filtered)
                return true
            }
        })
    }
}