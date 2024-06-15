import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.modulotech.workmanager.Lorraine
import io.modulotech.workmanager.db.converter.DataConverter
import io.modulotech.workmanager.db.converter.StringSetConverter
import io.modulotech.workmanager.db.dao.WorkerDao
import io.modulotech.workmanager.db.entity.WorkerEntity
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