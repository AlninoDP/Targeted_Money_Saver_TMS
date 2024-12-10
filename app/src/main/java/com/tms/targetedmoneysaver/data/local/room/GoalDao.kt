package com.tms.targetedmoneysaver.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals WHERE days_remaining > 0")
    fun getAllGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals ORDER BY days_saved DESC")
    fun getMostSavedGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE days_remaining = (SELECT MIN(days_remaining) FROM goals)")
    fun getClosestGoal(): LiveData<GoalEntity>

    @Query("SELECT * FROM goals ORDER BY date_started DESC")
    fun getRecentGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE days_remaining = 0")
    fun getCompletedGoals(): LiveData<List<GoalEntity>>

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalEntity>)

}