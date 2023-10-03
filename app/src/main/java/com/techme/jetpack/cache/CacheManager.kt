package com.techme.jetpack.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.techme.jetpack.model.Author
import com.techme.jetpack.util.AppGlobals

@Database(entities = [Author::class], version = 1)
abstract class CacheManager : RoomDatabase() {
    abstract val authorDao: AuthorDao

    companion object {
        private val database = Room.databaseBuilder(
            AppGlobals.getApplication(),
            CacheManager::class.java,
            "jetpack_cache"
        )
            //是否允许在主线程进行数据库操作
            .allowMainThreadQueries()
            // 设置数据库数据操作的线程池对象
            //.setQueryExecutor()
            // 监听数据库打开 创建的callback
            //.addCallback()
            // 设置room的日志输出模式
            // .setJournalMode()
            // 添加数据库升级的的具体实现逻辑
//            .addMigrations(object : Migration(1,2){
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("alert table author modify column feedCount varchar default 0")
//                }
//            })
            .build()

        @JvmStatic
        fun get(): CacheManager {
            return database
        }
    }
}