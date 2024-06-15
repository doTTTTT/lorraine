package io.modulotech.workmanager.work

data class Data internal constructor(
    val map: Map<String, Any?>
)

fun dataOf(vararg arg: Pair<String, Any?>): Data {
    val definition = DataDefinition()

    arg.forEach {
        definition.put(it.first, it.second)
    }

    return definition.build()
}

fun workData(block: DataDefinition.() -> Unit): Data {
    return DataDefinition().apply(block)
        .build()
}

class DataDefinition internal constructor() {

    private val map: MutableMap<String, Any?> = mutableMapOf()

    fun put(key: String, value: Any?) {
        if (value == null) {
            put(key, null)
        } else {
            when (value) {
                is String,
                is Boolean,
                is Int,
                is Long,
                is Float,
                is Double -> map[key] = value

                else -> throw IllegalArgumentException("Key $key has invalid type ${value::class}")
            }
        }
    }

    internal fun build(): Data {
        return Data(map)
    }

}