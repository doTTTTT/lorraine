package io.dot.lorraine

import dev.jordond.connectivity.Connectivity
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.dsl.LorraineRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    )

}

internal expect fun createUUID(): String


fun test() {
    val scope = CoroutineScope(Dispatchers.IO)
    val connectivity = Connectivity(scope = scope)

    connectivity.start()

    scope.launch {
        connectivity.statusUpdates.collect { status ->
            when (status) {
                is Connectivity.Status.Connected -> println("Connected to network")
                is Connectivity.Status.Disconnected -> println("Disconnected from network")
            }
        }
    }
}