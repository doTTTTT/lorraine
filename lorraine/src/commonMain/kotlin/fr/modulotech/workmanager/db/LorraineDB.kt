package fr.modulotech.workmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.db.converter.DataConverter
import fr.modulotech.workmanager.db.converter.StringSetConverter
import fr.modulotech.workmanager.db.dao.WorkerDao
import fr.modulotech.workmanager.db.entity.WorkerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    version = 1,
    entities = [
        WorkerEntity::class
    ]
)
@TypeConverters(
    StringSetConverter::class,
    DataConverter::class
)
internal abstract class LorraineDB : RoomDatabase() {

    abstract fun workerDao(): WorkerDao

}

internal fun initDatabase(db: RoomDatabase.Builder<LorraineDB>) {
    Lorraine.database = db.fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}