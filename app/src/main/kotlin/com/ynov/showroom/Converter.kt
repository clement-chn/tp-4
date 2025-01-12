package com.ynov.showroom

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromEquipmentsList(equipments: List<String>?): String? {
        return Gson().toJson(equipments)
    }

    @TypeConverter
    fun toEquipmentsList(equipmentsString: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(equipmentsString, type)
    }
}
