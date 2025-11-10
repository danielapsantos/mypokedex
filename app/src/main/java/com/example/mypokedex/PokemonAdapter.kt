package com.example.mypokedex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypokedex.model.Pokemon

class PokemonAdapter(
    pokemons: List<Pokemon>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private var pokemons: MutableList<Pokemon> = pokemons.toMutableList()
    private var displayedPokemons: List<Pokemon> = this.pokemons
    private var currentTypeFilter: String = "All"
    private var currentNameQuery: String = ""
    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)
        val tvPokemonId: TextView = itemView.findViewById(R.id.tvPokemonId)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = displayedPokemons[position]
        holder.tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        val formattedId = "#${pokemon.id.padStart(3, '0')}"
        holder.tvPokemonId.text = formattedId

        val primaryType = pokemon.types.firstOrNull() ?: "normal"
        val cardColor = getTypeColor(primaryType, holder.itemView.context)
        (holder.itemView as? CardView)?.setCardBackgroundColor(cardColor)

        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .into(holder.ivPokemon)

        holder.itemView.setOnClickListener {
            onItemClicked(pokemon.name)
        }
    }
    override fun getItemCount(): Int = displayedPokemons.size

    fun updateList(newList: List<Pokemon>) {
        this.pokemons.clear()
        this.pokemons.addAll(newList)
        displayedPokemons = this.pokemons
        notifyDataSetChanged()
    }
    fun filterByType(selectedType: String) {
        currentTypeFilter = selectedType.lowercase()
        applyFilters()
    }
    fun filter(query: String) {
        currentNameQuery = query.lowercase()
        applyFilters()
    }

    private fun applyFilters() {
        var filteredList = this.pokemons.toList()

        if (!currentTypeFilter.equals("all", ignoreCase = true)) {
            filteredList = filteredList.filter { pokemon ->
                pokemon.types.any { type ->
                    type.equals(currentTypeFilter, ignoreCase = true)
                }
            }
        }

        if (currentNameQuery.isNotBlank()) {
            filteredList = filteredList.filter { pokemon ->
                val matchesName = pokemon.name.lowercase().contains(currentNameQuery)
                val matchesId = pokemon.id.contains(currentNameQuery)
                matchesName || matchesId
            }
        }

        displayedPokemons = filteredList
        notifyDataSetChanged()
    }
    private fun getTypeColor(type: String, context: Context): Int {
        return when (type.lowercase()) {
            "normal" -> ContextCompat.getColor(context, R.color.type_normal)
            "fire" -> ContextCompat.getColor(context, R.color.type_fire)
            "water" -> ContextCompat.getColor(context, R.color.type_water)
            "grass" -> ContextCompat.getColor(context, R.color.type_grass)
            "electric" -> ContextCompat.getColor(context, R.color.type_electric)
            "ice" -> ContextCompat.getColor(context, R.color.type_ice)
            "fighting" -> ContextCompat.getColor(context, R.color.type_fighting)
            "poison" -> ContextCompat.getColor(context, R.color.type_poison)
            "ground" -> ContextCompat.getColor(context, R.color.type_ground)
            "flying" -> ContextCompat.getColor(context, R.color.type_flying)
            "psychic" -> ContextCompat.getColor(context, R.color.type_psychic)
            "bug" -> ContextCompat.getColor(context, R.color.type_bug)
            "rock" -> ContextCompat.getColor(context, R.color.type_rock)
            "ghost" -> ContextCompat.getColor(context, R.color.type_ghost)
            "dragon" -> ContextCompat.getColor(context, R.color.type_dragon)
            "steel" -> ContextCompat.getColor(context, R.color.type_steel)
            "dark" -> ContextCompat.getColor(context, R.color.type_dark)
            "fairy" -> ContextCompat.getColor(context, R.color.type_fairy)
            else -> ContextCompat.getColor(context, R.color.pokedex_text_secondary)
        }
    }
}