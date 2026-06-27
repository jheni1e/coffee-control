package com.example.mobilepucpr.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilepucpr.databinding.AdapterCoffeeBinding
import com.example.mobilepucpr.model.CoffeeItem

class CoffeesAdapter(private val coffees: List<CoffeeItem>, private val onClick: (CoffeeItem) -> Unit) : RecyclerView.Adapter<CoffeesAdapter.CoffeeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeHolder {
        val binding = AdapterCoffeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CoffeeHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeHolder, position: Int) {

        val coffee = coffees[position]

        holder.binding.txtName.text = coffee.name
        holder.binding.txtDescription.text = coffee.description
        holder.binding.txtCategory.text = coffee.category
        holder.binding.txtPrice.text = "R$ %.2f".format(coffee.price)

        holder.binding.root.setOnClickListener {
            onClick(coffee)
        }
    }

    override fun getItemCount(): Int = coffees.size

    class CoffeeHolder(val binding: AdapterCoffeeBinding) : RecyclerView.ViewHolder(binding.root)
}