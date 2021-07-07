package com.mazrou.boilerplate.perssistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mazrou.boilerplate.model.database.Ayat
import com.mazrou.boilerplate.model.database.RacineModel
import com.mazrou.boilerplate.model.database.Surah
import com.mazrou.boilerplate.model.database.World


@Database(
    entities = [
        Surah::class ,
        Ayat::class ,
        RacineModel::class ,
        World::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getQuranDao(): QuranDao


    companion object {
        const val DATABASE_NAME = "app_db"

        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }

        }

        private fun buildDatabase(context: Context): AppDataBase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }


    }
}