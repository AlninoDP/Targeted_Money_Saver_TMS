package com.tms.targetedmoneysaver.data

import android.net.Uri

data class Goal(
    var imageUri: Uri? = null,
    var title: String? = null,
    var amount: Int = 0,
    var description: String? = null,
    var category: String? = null,
    var period: Float = 1f,
    var dateStarted: String?  =null,
    var dailySavingAmount: Int = 0,
)
