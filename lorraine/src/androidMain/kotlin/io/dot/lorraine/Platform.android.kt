package io.dot.lorraine

import android.content.Context
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.getDatabaseBuilder
import io.dot.lorraine.db.initDatabase
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.work.LorraineWorker
import io.dot.lorraine.work.toLorraineInfo
import io.dot.lorraine.work.toWorkManagerData
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
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    ) {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<LorraineWorker>()
                .setInputData(lorraineRequest.toWorkManagerData())
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

internal actual fun createUUID(): String {
    return UUID.randomUUID().toString()
}