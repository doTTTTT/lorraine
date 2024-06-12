package fr.modulotech.workmanager.app.debug

sealed interface DebugAction {

    data object Add : DebugAction

}