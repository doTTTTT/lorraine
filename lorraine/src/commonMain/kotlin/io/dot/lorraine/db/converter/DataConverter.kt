package io.dot.lorraine.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import io.dot.lorraine.Lorraine
import io.dot.lorraine.work.Data
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

/**
 * Rethink way to store data
 */
@TypeConverters
internal class DataConverter {

    @TypeConverter
    fun typeFromJson(value: String): Data {
        val map = Lorraine.json.decodeFromString<Map<String, JsonPrimitive?>>(value)

        return Data(map.mapValues { entry -> entry.value?.contentOrNull })
    }

    @TypeConverter
    fun typeToJson(data: Data): String {
        return Lorraine.json.encodeToString(
            data.map
                .mapValues { entry ->
                    when (val value = entry.value) {
                        is Int -> JsonPrimitive(value)
                        is String -> JsonPrimitive(value)
                        is Double -> JsonPrimitive(value)

                        else -> null
                    }
                }
        )
    }

}
