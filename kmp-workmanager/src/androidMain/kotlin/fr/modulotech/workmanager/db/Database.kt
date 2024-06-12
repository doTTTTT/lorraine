package fr.modulotech.workmanager.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.modulotech.workmanager.LORRAINE_DATABASE

internal fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<LorraineDB> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(LORRAINE_DATABASE)

    return Room.databaseBuilder<LorraineDB>(
        context = appContext,
        name = dbFile.absolutePath
    )
}