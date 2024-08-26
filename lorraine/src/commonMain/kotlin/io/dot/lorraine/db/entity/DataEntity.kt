package io.dot.lorraine.db.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

private const val KEY = "key"
private const val VALUE = "value"

@Serializable
@SerialName("type")
internal sealed interface DataEntity {
    val key: String
    val value: Any
}

@Serializable
@SerialName("int")
internal data class IntData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Int
) : DataEntity

@Serializable
@SerialName("long")
internal data class LongData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Long
) : DataEntity

@Serializable
@SerialName("double")
internal data class DoubleData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Double
) : DataEntity

@Serializable
@SerialName("float")
internal data class FloatData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: Float
) : DataEntity

@Serializable
@SerialName("string")
internal data class StringData(
    @SerialName(KEY) override val key: String,
    @SerialName(VALUE) override val value: String
) : DataEntity

@Serializable
internal data object UnknownData : DataEntity {
    @Transient
    override val key: String = ""

    @Transient
    override val value: Any = Any()
}