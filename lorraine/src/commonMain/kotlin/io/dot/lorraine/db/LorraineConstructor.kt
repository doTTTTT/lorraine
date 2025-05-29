package io.dot.lorraine.db

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object LorraineConstructor : RoomDatabaseConstructor<LorraineDB> {
    override fun initialize(): LorraineDB
}