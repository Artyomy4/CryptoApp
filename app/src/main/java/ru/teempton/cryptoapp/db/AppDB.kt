package ru.teempton.cryptoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.teempton.cryptoapp.CoinPriceInfoDao
import ru.teempton.cryptoapp.pojo.CoinPriceInfo

@Database(entities = [CoinPriceInfo::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    companion object {
        private var db: AppDB? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDB {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDB::class.java, DB_NAME).build()
                db = instance
                return instance
            }
        }
    }
    abstract fun coinPriceInfoDao():CoinPriceInfoDao
}