package com.techme.jetpack.pages.login

import android.content.Intent
import com.techme.jetpack.cache.CacheManager
import com.techme.jetpack.model.Author
import com.techme.jetpack.util.AppGlobals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object UserManager {
    private val userFlow: MutableStateFlow<Author> = MutableStateFlow(Author())
    suspend fun save(author: Author) {
        CacheManager.get().authorDao.save(author)
        userFlow.emit(author)
    }

    fun isLogin(): Boolean {
        return userFlow.value.expiresTime > System.currentTimeMillis()
    }

    fun loginIfNeed() {
        if (isLogin()) {
            return
        } else {
            val intent = Intent(AppGlobals.getApplication(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            AppGlobals.getApplication().startActivity(intent)
        }
    }

    suspend fun getUser(): Flow<Author> {
        loadCache()
        return userFlow
    }

    suspend fun userId(): Long {
        loadCache()
        return userFlow.value.userId
    }

    private suspend fun loadCache() {
        if (!isLogin()) {
            val cache = CacheManager.get().authorDao.getUser()
            cache?.run {
                userFlow.emit(this)
            }
        }
    }
}