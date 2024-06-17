package io.dot.lorraine.constraint

import dev.jordond.connectivity.Connectivity
import io.dot.lorraine.dsl.LorraineConstraints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

internal class ConnectivityCheck : ConstraintCheck {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val connectivity = Connectivity(scope = scope)

    private var hasInternet = false

    init {
        connectivity.start()

        scope.launch {
            hasInternet = connectivity.status() is Connectivity.Status.Connected

            connectivity.statusUpdates
                .collect { status ->
                    hasInternet = when (status) {
                        is Connectivity.Status.Connected -> true
                        Connectivity.Status.Disconnected -> false
                    }
                }
        }
    }

    override fun match(constraints: LorraineConstraints): Boolean {
        if (!constraints.requireNetwork)
            return true

        return hasInternet
    }

}