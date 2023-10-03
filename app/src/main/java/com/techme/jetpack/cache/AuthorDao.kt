package com.techme.jetpack.cache

import androidx.room.*
import com.techme.jetpack.model.Author

// data access object
@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(author: Author): Long

    @Query("select * from author limit 1")
    suspend fun getUser(): Author?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(author: Author): Int

    @Delete
    suspend fun delete(author: Author): Int
}