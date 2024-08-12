package io.dot.lorraine.db

import androidx.room.Room
import androidx.room.RoomDatabase
import io.dot.lorraine.LORRAINE_DATABASE
import platform.Foundation.NSHomeDirectory

internal fun getDatabaseBuilder(): RoomDatabase.Builder<LorraineDB> {
    val dbFilePath = NSHomeDirectory() + "/$LORRAINE_DATABASE"

    return Room.databaseBuilder<LorraineDB>(
        name = dbFilePath,
        factory = { TODO() }
    )
}

