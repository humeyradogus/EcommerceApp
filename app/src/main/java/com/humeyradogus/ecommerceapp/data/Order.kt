package com.humeyradogus.ecommerceapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


data class Order(
    val orderStatus: String,
    val totalPrice:Float,
    val products:List<CartProduct>,
    val address:Address)
