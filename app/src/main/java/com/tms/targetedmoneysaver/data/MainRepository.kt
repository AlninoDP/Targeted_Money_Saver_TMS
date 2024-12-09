package com.tms.targetedmoneysaver.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.data.local.room.GoalDao
import com.tms.targetedmoneysaver.data.remote.requestbody.UpdateSavingBody
import com.tms.targetedmoneysaver.data.remote.response.UpdateSavingResponse
import com.tms.targetedmoneysaver.data.remote.retrofit.ApiService

class MainRepository(
    private val apiService: ApiService,
    private val goalDao: GoalDao
) {

    fun getAllGoals(): LiveData<Result<List<GoalEntity>>> = liveData {
        emit(Result.Loading(true))
        try {
            val response = apiService.getDreamProduct()
            val goalItems = response.data
            val goalList = goalItems.map { goalItem ->
                GoalEntity(
                    id = goalItem.id,
                    image = goalItem.goal.image,
                    title = goalItem.goal.title,
                    amount = goalItem.goal.amount,
                    description = goalItem.goal.description,
                    category = goalItem.goal.category,
                    period = goalItem.goal.period,
                    dateStarted = goalItem.goal.dateStarted,
                    amountSaved = goalItem.tracker.amountSaved,
                    dailySave = goalItem.tracker.dailySave,
                    daysSaved = goalItem.tracker.daysSaved,
                    daysRemaining = goalItem.tracker.daysRemaining
                )
            }

            goalDao.deleteAllGoals()
            goalDao.insertGoals(goalList)
            emit(Result.Loading(false))
        } catch (e: Exception) {
            goalDao.deleteAllGoals()
            emit(Result.Loading(false))
            // Emit an empty list when error occurs
            emit(Result.Success(emptyList()))
            return@liveData // Return early to prevent the rest of the code from running
        }

        val localData: LiveData<Result<List<GoalEntity>>> =
            goalDao.getAllGoals().map { Result.Success(it) }

        emitSource(localData)
    }

    fun updateSaving(id: String): LiveData<Result<UpdateSavingResponse>> = liveData {
        emit(Result.Loading(true))
        try {
            val response = apiService.updateSaving(UpdateSavingBody(id))
            emit(Result.Loading(false))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Loading(false))
            emit(Result.Failure(e))
        }


    }

    fun getClosestGoal(): LiveData<GoalEntity> {
        return goalDao.getClosestGoal()
    }

    fun getRecentGoals(): LiveData<List<GoalEntity>> {
        return goalDao.getRecentGoals()
    }

    fun getMostSavedGoals(): LiveData<List<GoalEntity>> {
        return goalDao.getMostSavedGoals()
    }


}