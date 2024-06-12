package fr.dot.lorraine.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform