package com.example.mypokedex

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemons: List<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a list of Pokémon using images from the drawable folder.
        pokemons = listOf(
            Pokemon("Bulbasaur", R.drawable.bulbasaur),
            Pokemon("Charmander", R.drawable.charmander),
            Pokemon("Squirtle", R.drawable.squirtle),
            Pokemon("Pikachu", R.drawable.pikachu)
        )

        recyclerView = findViewById(R.id.rvPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(pokemons)
        recyclerView.adapter = adapter

        // Configure the search bar
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = pokemons.filter {
                    it.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateList(filteredList)
                return true
            }
        })
    }
}