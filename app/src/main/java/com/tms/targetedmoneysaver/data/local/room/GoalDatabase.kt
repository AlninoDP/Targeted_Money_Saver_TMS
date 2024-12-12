package com.tms.targetedmoneysaver.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity

@Database(entities = [GoalEntity::class], version = 1, exportSchema = false)
abstract class GoalDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var instance: GoalDatabase? = null
        fun getInstance(context: Context): GoalDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext, GoalDatabase::class.java, "goals.db"
                ).build()
            }
    }

}
