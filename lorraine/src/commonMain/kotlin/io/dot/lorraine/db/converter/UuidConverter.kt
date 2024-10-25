package io.dot.lorraine.db.converter

import androidx.room.TypeConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class UuidConverter {

    @TypeConverter
    fun uuidFromString(value: String): Uuid {
        return Uuid.Companion.parse(value)
    }

    @TypeConverter
    fun stringFromUuid(uuid: Uuid): String {
        return uuid.toString()
    }

}