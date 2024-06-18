package io.dot.lorraine

import io.dot.lorraine.db.getDatabaseBuilder
import io.dot.lorraine.db.initDatabase

fun Lorraine.initialize() {
    val db = getDatabaseBuilder()

    platform = IOSPlatform()

    initDatabase(db)
}