package fr.modulotech.workmanager

import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.dsl.LorraineRequest

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        lorraineRequest: LorraineRequest
    )

}

expect fun createUUID(): String