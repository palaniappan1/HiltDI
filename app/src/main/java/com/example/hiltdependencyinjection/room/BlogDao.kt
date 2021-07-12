package com.example.hiltdependencyinjection.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hiltdependencyinjection.model.Blog

@Dao
interface BlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogCacheEntity: BlogCacheEntity) : Long

    @Query("SELECT * FROM blogs")
    suspend fun getAllData() : List<BlogCacheEntity>
}