package fr.modulotech.workmanager.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import fr.modulotech.workmanager.db.converter.DataConverter
import fr.modulotech.workmanager.db.converter.StringSetConverter
import fr.modulotech.workmanager.work.Data
import fr.modulotech.workmanager.work.LorraineInfo

@Entity(
    primaryKeys = ["id"],
    tableName = WorkerEntity.TABLE_NAME
)
internal data class WorkerEntity(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "identifier")
    val identifier: String,

    @ColumnInfo(name = "state")
    val state: LorraineInfo.State,

    @ColumnInfo(name = "tags")
    @TypeConverters(StringSetConverter::class)
    val tags: Set<String>,

    @ColumnInfo(name = "input_data")
    @TypeConverters(DataConverter::class)
    val inputData: Data?

) {

    companion object {
        const val TABLE_NAME = "worker"
    }

}

internal fun WorkerEntity.toInfo() = LorraineInfo(
    id = id,
    state = state,
    tags = tags
)