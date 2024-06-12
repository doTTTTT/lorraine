package fr.modulotech.workmanager.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.modulotech.workmanager.Lorraine
import kotlinx.serialization.encodeToString

@TypeConverters
internal class StringSetConverter {

    @TypeConverter
    fun typeFromJson(value: String): Set<String> = Lorraine.json.decodeFromString(value)

    @TypeConverter
    fun typeToJson(list: Set<String>): String = Lorraine.json.encodeToString(list)

}