package com.example.mypokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypokedex.model.Pokemon

class PokemonAdapter(
    private var pokemons: List<Pokemon>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }

        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .into(holder.ivPokemon)
    }
    override fun getItemCount(): Int = pokemons.size

    fun updateList(newList: List<Pokemon>) {
        pokemons = newList
        notifyDataSetChanged()
    }
}