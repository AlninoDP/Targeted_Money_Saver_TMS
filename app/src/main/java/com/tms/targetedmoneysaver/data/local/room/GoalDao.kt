package com.tms.targetedmoneysaver.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals ")
    fun getAllGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE days_remaining > 0 ORDER BY days_saved DESC")
    fun getMostSavedGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE days_remaining = (SELECT MIN(days_remaining) FROM goals WHERE days_remaining > 0)")
    fun getClosestGoal(): LiveData<GoalEntity>

    @Query("SELECT * FROM goals WHERE days_remaining = 0")
    fun getCompletedGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE days_remaining = (SELECT MIN(days_remaining) FROM goals WHERE days_remaining > 0) ")
    suspend fun getClosestGoalForNotification(): GoalEntity?

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalEntity>)

}