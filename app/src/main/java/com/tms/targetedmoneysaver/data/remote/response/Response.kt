package com.tms.targetedmoneysaver.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Response(
	val data: List<DataItem?>? = null,
	val message: String? = null
) : Parcelable

@Parcelize
data class DataItem(
	val photoURL: String? = null,
	val product: Product? = null,
	val tracker: Tracker? = null,
	val id: String? = null,
	val goals: Goals? = null
) : Parcelable

@Parcelize
data class Product(
	val price: Int? = null,
	val title: String? = null,
	val productUrl: String? = null
) : Parcelable

@Parcelize
data class Goals(
	val dateStart: String? = null,
	val days: Int? = null
) : Parcelable

@Parcelize
data class Tracker(
	val saved: Int? = null,
	val daysSaved: Int? = null,
	val daysRemaining: Int? = null
) : Parcelable
