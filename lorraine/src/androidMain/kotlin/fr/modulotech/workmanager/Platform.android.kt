package fr.modulotech.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.db.getDatabaseBuilder
import fr.modulotech.workmanager.dsl.WorkRequest
import fr.modulotech.workmanager.work.LorraineInfo
import fr.modulotech.workmanager.work.LorraineWorker
import fr.modulotech.workmanager.work.toWorkManagerData
import initDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.CoroutineContext

internal class AndroidPlatform(
    private val workManager: WorkManager
) : Platform, CoroutineScope {
    override val name: String = "android"

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    init {
        launch {
            workManager.getWorkInfosFlow(WorkQuery.fromStates(WorkInfo.State.entries))
                .map { list -> list.map(WorkInfo::toLorraineInfo) }
                .collect { info ->
                    // TODO Update LorrainDB
                }
        }
    }

    override fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        workRequest: WorkRequest
    ) {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<LorraineWorker>()
                .setInputData(workRequest.toWorkManagerData())
                .setConstraints(Constraints.NONE)
                .build()
        )
    }
}

fun Lorraine.initialize(context: Context) {
    val db = getDatabaseBuilder(context)

    platform = AndroidPlatform(WorkManager.getInstance(context))

    initDatabase(db)
}

actual fun createUUID(): String {
    return UUID.randomUUID().toString()
}

internal fun WorkInfo.toLorraineInfo() = LorraineInfo(
    id = id.toString(),
    state = when (state) {
        WorkInfo.State.ENQUEUED -> LorraineInfo.State.ENQUEUED
        WorkInfo.State.RUNNING -> LorraineInfo.State.RUNNING
        WorkInfo.State.SUCCEEDED -> LorraineInfo.State.SUCCEEDED
        WorkInfo.State.FAILED -> LorraineInfo.State.FAILED
        WorkInfo.State.BLOCKED -> LorraineInfo.State.BLOCKED
        WorkInfo.State.CANCELLED -> LorraineInfo.State.CANCELLED
    },
    tags = tags
)