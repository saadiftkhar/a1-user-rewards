/*
 *
 * Created by Saad Iftikhar on 8/25/21, 6:05 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTime {

    fun getYear(): Int {
        return Calendar.getInstance()[Calendar.YEAR]
    }

    fun getMonth(): Int {
        return Calendar.getInstance()[Calendar.MONTH + 1]
    }

    fun getDay(): Int {
        return Calendar.getInstance()[Calendar.DAY_OF_MONTH]
    }

    fun getHour(): Int {
        return Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    }

    fun getMin(): Int {
        return Calendar.getInstance()[Calendar.MINUTE]
    }

    fun getFormat(): Int {
        return Calendar.getInstance()[Calendar.AM_PM]
    }

    fun currentDate(): String? {
        val time = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun currentTime(): String? {
        val time = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(time)
    }

    fun getTimestampDate(timestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000
        return DateFormat.format("EE dd MMM", cal).toString()
    }

    fun getTimestampTime(timestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000
        return DateFormat.format("hh:mm a", cal).toString()
    }

}
