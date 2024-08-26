package io.dot.lorraine.db.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val KEY = "key"
private const val VALUE = "value"

@Serializable
@SerialName("type")
internal sealed interface DataEntity<T> {
    val key: String
    val value: T
}

@Serializable
@SerialName("int")
internal data class IntData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Int
) : DataEntity<Int>

@Serializable
@SerialName("long")
internal data class LongData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Long
) : DataEntity<Long>

@Serializable
@SerialName("double")
internal data class DoubleData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Double
) : DataEntity<Double>

@Serializable
@SerialName("float")
internal data class FloatData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Float
) : DataEntity<Float>

@Serializable
@SerialName("string")
internal data class StringData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: String
) : DataEntity<String>

@Serializable
internal data object UnknownData : DataEntity<Any> {
    override val key: String
        get() = ""
    override val value: Any = Any()
}