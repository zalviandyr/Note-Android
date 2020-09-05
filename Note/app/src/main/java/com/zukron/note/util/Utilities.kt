package com.zukron.note.util

import android.text.Editable
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object Utilities {
    fun LocalDate.formatToString(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLL")
        return this.format(dateTimeFormatter)
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}