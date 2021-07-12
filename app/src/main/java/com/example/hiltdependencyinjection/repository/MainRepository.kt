package com.example.hiltdependencyinjection.repository

import com.example.hiltdependencyinjection.model.Blog
import com.example.hiltdependencyinjection.retrofit.BlogRetrofit
import com.example.hiltdependencyinjection.retrofit.NetworkMapper
import com.example.hiltdependencyinjection.room.BlogCacheEntity
import com.example.hiltdependencyinjection.room.BlogDao
import com.example.hiltdependencyinjection.room.CacheMapper
import com.example.hiltdependencyinjection.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class MainRepository constructor(
    private val blogDao: BlogDao,
    private val blogRetrofit: BlogRetrofit,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
) {

    suspend fun getBlog(): Flow<DataState<List<Blog>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        try {
            val netWorkBlogs = blogRetrofit.getBlogs()
            val blogs: List<Blog> = networkMapper.mapFromEntityList(netWorkBlogs)
            for (blog in blogs) {
                blogDao.insert(cacheMapper.mapToEntity(blog))
            }
            val cachedBlogs: List<BlogCacheEntity> = blogDao.getAllData()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBlogs)))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}