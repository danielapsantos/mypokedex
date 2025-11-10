package com.example.mypokedex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.model.Pokemon
import com.example.mypokedex.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView
    private var allPokemons = listOf<Pokemon>()

    private lateinit var spinnerTypeFilter: Spinner
    private val pokemonTypes = listOf(
        "All", "Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Fighting",
        "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost",
        "Dragon", "Steel", "Dark", "Fairy"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.navigationIcon = null

        recyclerView = findViewById(R.id.rvPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(emptyList()) {pokemonName ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("POKEMON_NAME", pokemonName)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

    lifecycleScope.launch {
        fetchPokemons()
    }
        setupTypeFilter()
    setupSearch()
}
    private fun setupTypeFilter() {
        spinnerTypeFilter = findViewById(R.id.spinnerTypeFilter)

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            pokemonTypes
        )
        spinnerTypeFilter.adapter = spinnerAdapter

        spinnerTypeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = pokemonTypes[position]

                adapter.filterByType(selectedType)

                findViewById<SearchView>(R.id.searchView).setQuery("", false)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private suspend fun fetchPokemons() {
        try {
            val listResponse = RetrofitInstance.api.getPokemons()
            val results = listResponse.results

            val deferredPokemons = results.map { result ->
                lifecycleScope.async(Dispatchers.IO) {
                    val id = result.url.split("/").dropLast(1).last()
                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

                    val detailResponse = try {
                        RetrofitInstance.api.getPokemonDetail(result.name)
                    } catch (e: Exception) {
                        Log.e("PokedexApp", "Error retrieving details of ${result.name}: ${e.message}")
                        null
                    }

                    val types = detailResponse?.types?.map { it.type.name } ?: emptyList()

                    Pokemon(result.name, imageUrl, id, types)
                }
            }

            allPokemons = deferredPokemons.awaitAll()

            withContext(Dispatchers.Main) {
                Log.d("PokedexApp", "Final list ready. Size: ${allPokemons.size}")
                adapter.updateList(allPokemons)
            }

        } catch (e: Exception) {
            Log.e("PokedexApp", "Main request failed: ${e.message}")
        }
    }
    private fun setupSearch() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })
    }
}