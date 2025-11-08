package com.example.mypokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a list of Pokémon using images from the drawable folder.
        val pokemons = listOf(
            Pokemon("Bulbasaur", R.drawable.bulbasaur),
            Pokemon("Charmander", R.drawable.charmander),
            Pokemon("Squirtle", R.drawable.squirtle),
            Pokemon("Pikachu", R.drawable.pikachu)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rvPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PokemonAdapter(pokemons)
    }
}