package com.tms.targetedmoneysaver.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "goals")
@Parcelize
class GoalEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "period")
    val period: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "date_started")
    val dateStarted: String,

    @ColumnInfo(name = "amount_saved")
    val amountSaved: Int,

    @ColumnInfo(name = "daily_save")
    val dailySave: Int,

    @ColumnInfo(name = "days_saved")
    val daysSaved: Int,

    @ColumnInfo(name = "days_remaining")
    val daysRemaining: Int
) : Parcelable