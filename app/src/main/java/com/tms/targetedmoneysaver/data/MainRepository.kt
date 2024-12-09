package com.tms.targetedmoneysaver.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tms.targetedmoneysaver.data.remote.response.Goal
import com.tms.targetedmoneysaver.data.remote.response.GoalItem
import com.tms.targetedmoneysaver.data.remote.response.GoalProduct
import com.tms.targetedmoneysaver.data.remote.response.GoalTracker
import com.tms.targetedmoneysaver.data.remote.retrofit.ApiService

class MainRepository(
    private val apiService: ApiService
) {

    fun getAllGoals(): LiveData<Result<List<GoalItem>>> = liveData {
        emit(Result.Loading(true))

        // Simulate Data
        val listGoal: List<GoalItem> = listOf(
            GoalItem(
                goal = Goal("image1", "Poco M3", 30, "01/12/2024"),
                product = GoalProduct(1200000, "Poco M3"),
                tracker = GoalTracker(30, 0, 0,40000)
            ),
            GoalItem(
                goal = Goal("image2", "Lenovo Legion Pro 7i", 60, "03/12/2024"),
                product = GoalProduct(42000000, "Lenovo Legion Pro 7i"),
                tracker = GoalTracker(60, 0, 0, 700000)
            ),
            GoalItem(
                goal = Goal("image3", "TWS Mi Buds 4", 30, "28/11/2024"),
                product = GoalProduct(289000, "TWS Mi Buds 4"),
                tracker = GoalTracker(24, 6, 58200, 9700)
            )
        )

        emit(Result.Success(listGoal))
    }
}