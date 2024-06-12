package fr.modulotech.workmanager.db.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.work.Data
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