package com.spotlight.spotlightapp.data.dao

import androidx.room.*
import com.spotlight.spotlightapp.data.task.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getCategories(): List<Category>

    @Insert
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}