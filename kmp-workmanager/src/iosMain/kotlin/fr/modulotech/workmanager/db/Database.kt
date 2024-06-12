package fr.modulotech.workmanager.db

import androidx.room.Room
import androidx.room.RoomDatabase
import fr.modulotech.workmanager.LORRAINE_DATABASE
import platform.Foundation.NSHomeDirectory

internal fun getDatabaseBuilder(): RoomDatabase.Builder<LorraineDB> {
    val dbFilePath = NSHomeDirectory() + "/$LORRAINE_DATABASE"

    return Room.databaseBuilder<LorraineDB>(
        name = dbFilePath,
        factory =  { LorraineDB::class.instantiateImpl() }
    )
}

