package com.example.mobilepucpr.controller

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilepucpr.databinding.ActivityAddEditCoffeeBinding
import com.example.mobilepucpr.model.CoffeeItem
import com.example.mobilepucpr.model.Database

class AddEditCoffeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditCoffeeBinding
    private lateinit var database: Database

    private var coffeeId: Long = -1
    private var coffee: CoffeeItem? = null

    private val categories = listOf(
        "Cafés",
        "Bebidas Geladas",
        "Doces",
        "Salgados",
        "Chás"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditCoffeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Database(this)
        setupSpinner()

        coffeeId = intent.getLongExtra("coffeeId", -1)

        if (coffeeId != -1L) {
            loadCoffee()
        } else {
            binding.toolbar.title = "Novo Produto"
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnSave.setOnClickListener {
            saveCoffee()
        }

        binding.btnDelete.setOnClickListener {
            deleteCoffee()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        binding.spnCategory.adapter = adapter
    }

    private fun loadCoffee() {
        coffee = database.getCoffeeById(coffeeId)

        coffee?.let {

            binding.toolbar.title = "Editar Produto"

            binding.edtName.setText(it.name)
            binding.edtDescription.setText(it.description)
            binding.edtPrice.setText(it.price.toString())

            val index = categories.indexOf(it.category)
            if (index >= 0) {
                binding.spnCategory.setSelection(index)
            }
        }
    }

    private fun saveCoffee() {
        val name = binding.edtName.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val category = binding.spnCategory.selectedItem.toString()
        val price = binding.edtPrice.text.toString()
            .replace(",", ".")
            .toFloatOrNull()

        if (name.isBlank() ||
            description.isBlank() ||
            price == null
        ) {
            Toast.makeText(
                this,
                "Preencha todos os campos corretamente.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (coffee == null) {
            val newCoffee = CoffeeItem(
                name,
                description,
                category,
                price
            )
            database.addCoffeeItem(newCoffee)

            Toast.makeText(
                this,
                "Produto cadastrado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            coffee?.apply {
                this.name = name
                this.description = description
                this.category = category
                this.price = price
            }

            database.editCoffeeItem(this.coffee!!)

            Toast.makeText(
                this,
                "Produto atualizado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }

    private fun deleteCoffee() {
        coffee?.let {
            database.removeCoffeeItem(it)

            Toast.makeText(
                this,
                "Produto removido com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}