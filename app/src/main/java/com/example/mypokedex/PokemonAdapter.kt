package com.example.mypokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(
    private var pokemons: List<Pokemon>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    // ViewHolder: connects the layout elements (item_pokemon.xml)
    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)
    }

    // Creates the layout for each item in the list.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    // Defines the data for each Pokémon in the screen elements
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.ivPokemon.setImageResource(pokemon.imageResId)
        holder.tvPokemonName.text = pokemon.name
    }

    // Returns the total number of items
    override fun getItemCount(): Int = pokemons.size

    // Update the Pokémon list by filtering
    fun updateList(newList: List<Pokemon>) {
        pokemons = newList
        notifyDataSetChanged()
    }
}
