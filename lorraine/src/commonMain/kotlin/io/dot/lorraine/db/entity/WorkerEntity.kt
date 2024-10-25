package io.dot.lorraine.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import io.dot.lorraine.db.converter.DataConverter
import io.dot.lorraine.db.converter.StringSetConverter
import io.dot.lorraine.work.Data
import io.dot.lorraine.work.LorraineInfo

@Entity(
    primaryKeys = ["id"],
    tableName = WorkerEntity.TABLE_NAME
)
internal data class WorkerEntity(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "queue_id")
    val queueId: String,

    @ColumnInfo(name = "identifier")
    val identifier: String,

    @ColumnInfo(name = "state")
    val state: LorraineInfo.State,

    @ColumnInfo(name = "tags")
    @TypeConverters(StringSetConverter::class)
    val tags: Set<String>,

    @ColumnInfo(name = "worker_dependencies")
    @TypeConverters(StringSetConverter::class)
    val workerDependencies: Set<String>,

    @ColumnInfo(name = "input_data")
    @TypeConverters(DataConverter::class)
    val inputData: Data? = null,

    @ColumnInfo(name = "output_data")
    @TypeConverters(DataConverter::class)
    val outputData: Data? = null,

    @Embedded(prefix = "constraints_")
    val constraints: ConstraintEntity

) {

    companion object {
        const val TABLE_NAME = "worker"
    }

}

internal fun WorkerEntity.toInfo() = LorraineInfo(
    id = id,
    state = state,
    identifier = identifier,
    inputData = inputData,
    outputData = outputData,
    tags = tags
)