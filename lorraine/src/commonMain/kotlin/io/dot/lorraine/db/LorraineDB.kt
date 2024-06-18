package io.dot.lorraine.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.dot.lorraine.db.converter.DataConverter
import io.dot.lorraine.db.converter.StringSetConverter
import io.dot.lorraine.db.dao.WorkerDao
import io.dot.lorraine.db.entity.WorkerEntity

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
internal abstract class LorraineDB : RoomDatabase(), DB {

    abstract fun workerDao(): WorkerDao

}

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables() {}
}