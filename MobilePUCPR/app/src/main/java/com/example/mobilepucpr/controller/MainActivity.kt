package com.example.mobilepucpr.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilepucpr.databinding.ActivityMainBinding
import com.example.mobilepucpr.model.CoffeeItem
import com.example.mobilepucpr.model.Database
import com.example.mobilepucpr.view.CoffeesAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: Database
    private val coffees = mutableListOf<CoffeeItem>()
    private lateinit var adapter: CoffeesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = Database(this)

        setupRecyclerView()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        loadCoffees()
    }

    private fun setupRecyclerView() {
        adapter = CoffeesAdapter(coffees) { coffee ->
            val intent = Intent(this, AddEditCoffeeActivity::class.java)
            intent.putExtra("coffeeId", coffee.id)
            startActivity(intent)
        }

        binding.rcvCoffees.layoutManager = LinearLayoutManager(this)
        binding.rcvCoffees.adapter = adapter
    }

    private fun setupButtons() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditCoffeeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCoffees() {
        coffees.clear()
        coffees.addAll(database.getAllCoffees())
        adapter.notifyDataSetChanged()
        updateSubtitle()
    }

    private fun updateSubtitle() {
        binding.txtSubtitle.text =
            if (coffees.isEmpty())
                "Nenhum produto cadastrado."
            else
                "${coffees.size} produto(s) cadastrado(s)"
    }
}