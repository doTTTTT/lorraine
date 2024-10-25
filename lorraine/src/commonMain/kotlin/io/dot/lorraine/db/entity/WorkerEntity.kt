@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import io.dot.lorraine.db.converter.DataConverter
import io.dot.lorraine.db.converter.StringSetConverter
import io.dot.lorraine.work.LorraineData
import io.dot.lorraine.work.LorraineInfo
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    primaryKeys = ["uuid"],
    tableName = WorkerEntity.TABLE_NAME
)
internal data class WorkerEntity(

    @ColumnInfo(name = "uuid")
//    @TypeConverters(UuidConverter::class) When Uuid is stable
    val uuid: String,

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
    val inputData: LorraineData? = null,

    @ColumnInfo(name = "output_data")
    @TypeConverters(DataConverter::class)
    val outputData: LorraineData? = null,

    @Embedded(prefix = "constraints_")
    val constraints: ConstraintEntity

) {

    companion object {
        const val TABLE_NAME = "worker"
    }

}

internal fun WorkerEntity.toInfo() = LorraineInfo(
    uuid = Uuid.parse(uuid),
    state = state,
    identifier = identifier,
    inputData = inputData,
    outputData = outputData,
    tags = tags
)