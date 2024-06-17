package io.dot.lorraine.constraint

import dev.jordond.connectivity.AppleConnectivity
import io.dot.lorraine.dsl.LorraineConstraints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal actual object ConnectivityCheck : ConstraintCheck {
    private var hasInternet: Boolean = false

    val connectivity = AppleConnectivity()
    val scope = CoroutineScope(Dispatchers.Main)

    init {
        println("ConnectivityCheck 1")
        connectivity.start()
        println("ConnectivityCheck 2: ${connectivity.isMonitoring.value}")

        scope.launch {
            connectivity.updates
                .collect {
                    println("ConnectivityCheck updates: $it")
                }
        }
        scope.launch {
            connectivity.isMonitoring
                .collect {
                    println("ConnectivityCheck monitor: $it")
                    println("ConnectivityCheck updates 2: ${connectivity.updates.value}")
                }
        }
        scope.launch {
            connectivity.statusUpdates
                .collect {
                    println("ConnectivityCheck status: $it")
                }
        }
    }

    override suspend fun match(constraints: LorraineConstraints): Boolean {
        return true//connectivity.status().isConnected
    }

}
