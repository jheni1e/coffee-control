package com.example.mobilepucpr.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "coffees.db"
        const val DATABASE_VERSION = 1
        const val DB_TABLE = "coffee_items"
        const val DB_FIELD_ID = "id"
        const val DB_FIELD_NAME = "name"
        const val DB_FIELD_DESCRIPTION = "description"
        const val DB_FIELD_CATEGORY = "category"
        const val DB_FIELD_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
                "$DB_FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DB_FIELD_NAME TEXT, " +
                "$DB_FIELD_DESCRIPTION TEXT, " +
                "$DB_FIELD_CATEGORY TEXT, " +
                "$DB_FIELD_PRICE REAL);"

        val db = db ?: return

        db.beginTransaction()
        try {
            db.execSQL(sql)
            db.setTransactionSuccessful()
        }
        catch (e: Exception) {
            Log.d("CoffeesApp", e.localizedMessage)
        }
        finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getAllCoffees(): MutableList<CoffeeItem> {
        val coffees = mutableListOf<CoffeeItem>()
        val db = readableDatabase
        val cursor = db.query(
            DB_TABLE,
            null,
            null,
            null,
            null,
            null,
            DB_FIELD_NAME
        )
        with(cursor) {
            while(moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val name = getString(getColumnIndexOrThrow(DB_FIELD_NAME))
                val description = getString(getColumnIndexOrThrow(DB_FIELD_DESCRIPTION))
                val category = getString(getColumnIndexOrThrow(DB_FIELD_CATEGORY))
                val price = getFloat(getColumnIndexOrThrow(DB_FIELD_PRICE))
                val coffeeItem = CoffeeItem(name, description, category, price)
                coffeeItem.id = id
                coffees.add(coffeeItem)
            }
        }
        cursor.close()

        return coffees
    }

    fun addCoffeeItem(item: CoffeeItem): Long {
        var id: Long = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_NAME, item.name)
            put(DB_FIELD_DESCRIPTION, item.description)
            put(DB_FIELD_CATEGORY, item.category)
            put(DB_FIELD_PRICE, item.price)
        }

        db.beginTransaction()
        try {
            id = db.insert(DB_TABLE, null, values)
            db.setTransactionSuccessful()
        }
        catch (e: Exception) {
            Log.d("CoffeesApp", e.localizedMessage)
        }
        finally {
            db.endTransaction()
        }

        return id
    }

    fun editCoffeeItem(item: CoffeeItem): Int {
        var count = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_NAME, item.name)
            put(DB_FIELD_DESCRIPTION, item.description)
            put(DB_FIELD_CATEGORY, item.category)
            put(DB_FIELD_PRICE, item.price)
        }

        val selection = "$DB_FIELD_ID = ?"
        val arg = arrayOf(item.id.toString())

        db.beginTransaction()
        try {
            count = db.update(DB_TABLE, values, selection, arg)
            db.setTransactionSuccessful()
        }
        catch (e: Exception) {
            Log.d("CoffeesApp", e.localizedMessage)
        }
        finally {
            db.endTransaction()
        }

        return count
    }

    fun removeCoffeeItem(item: CoffeeItem): Int {
        var count = 0
        val db = writableDatabase

        val selection = "$DB_FIELD_ID = ?"
        val arg = arrayOf(item.id.toString())

        db.beginTransaction()
        try {
            count = db.delete(DB_TABLE, selection, arg)
            db.setTransactionSuccessful()
        }
        catch (e: Exception) {
            Log.d("CoffeesApp", e.localizedMessage)
        }
        finally {
            db.endTransaction()
        }

        return count
    }

    fun getCoffeeById(id: Long): CoffeeItem? {
        val db = readableDatabase
        val cursor = db.query(
            DB_TABLE,
            null,
            "$DB_FIELD_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var coffee: CoffeeItem? = null
        if (cursor.moveToFirst()) {
            coffee = CoffeeItem(
                cursor.getString(cursor.getColumnIndexOrThrow(DB_FIELD_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DB_FIELD_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DB_FIELD_DESCRIPTION)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DB_FIELD_PRICE))
            )
            coffee.id = cursor.getLong(cursor.getColumnIndexOrThrow(DB_FIELD_ID))
        }
        cursor.close()

        return coffee
    }
}