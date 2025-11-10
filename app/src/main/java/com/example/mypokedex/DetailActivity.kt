package com.example.mypokedex
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mypokedex.model.PokemonDetail
import com.example.mypokedex.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Details"

        val pokemonName = intent.getStringExtra("POKEMON_NAME")

        if (pokemonName != null) {
            lifecycleScope.launch {
                fetchPokemonDetails(pokemonName.lowercase())
            }
        }
    }

    private suspend fun fetchPokemonDetails(name: String) {
        withContext(Dispatchers.IO) {
            try {
                val detail = RetrofitInstance.api.getPokemonDetail(name)

                withContext(Dispatchers.Main) {
                    bindDetails(detail)
                }
            } catch (e: Exception) {
                Log.e("DetailActivity", "Failed to fetch details for $name: ${e.message}")
            }
        }
    }

    private fun bindDetails(detail: PokemonDetail) {

        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${detail.id}.png"

        findViewById<TextView>(R.id.tvName).text = detail.name.replaceFirstChar { it.uppercase() }
        findViewById<TextView>(R.id.tvId).text = "#${detail.id}"
        findViewById<TextView>(R.id.tvHeight).text = "Height: ${detail.height * 10} cm"
        findViewById<TextView>(R.id.tvWeight).text = "Weight: ${detail.weight / 10.0} kg"

        Glide.with(this)
            .load(imageUrl)
            .into(findViewById<ImageView>(R.id.ivDetailImage))

        val typesString = detail.types.joinToString(", ") { it.type.name.uppercase() }
        findViewById<TextView>(R.id.tvTypes).text = "Types: $typesString"

        val hp = detail.stats.firstOrNull { it.stat.name == "hp" }?.base_stat ?: 0
        val attack = detail.stats.firstOrNull { it.stat.name == "attack" }?.base_stat ?: 0
        val defense = detail.stats.firstOrNull { it.stat.name == "defense" }?.base_stat ?: 0

        findViewById<TextView>(R.id.tvHp).text = "HP: $hp"
        findViewById<TextView>(R.id.tvAttack).text = "Attack: $attack"
        findViewById<TextView>(R.id.tvDefense).text = "Defense: $defense"
    }
}