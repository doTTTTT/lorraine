package io.dot.lorraine.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import io.dot.lorraine.Lorraine
import io.dot.lorraine.work.Data
import kotlinx.serialization.encodeToString

@TypeConverters
internal class DataConverter {

    @TypeConverter
    fun typeFromJson(value: String): Data {
        val map = Lorraine.json.decodeFromString<Map<String, Any?>>(value)

        return Data(map)
    }

    @TypeConverter
    fun typeToJson(data: Data): String {
        return Lorraine.json.encodeToString(data.map)
    }

}