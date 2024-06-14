package ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class LogBody(

    @SerialName("ddsource")
    val ddSource: String,

    @SerialName("ddtags")
    val ddTags: String,

    @SerialName("hostname")
    val hostname: String,

    @SerialName("message")
    val message: String,

    @SerialName("service")
    val service: String

) {

    @Serializable
    data class Log(

        @SerialName("status_code")
        val statusCode: Int,

        @SerialName("method")
        val method: String,

        @SerialName("request")
        val request: Body,

        @SerialName("response")
        val response: Body

    )

    @Serializable
    data class Body(

        @SerialName("headers")
        val headers: Map<String, List<String>>,

        @SerialName("body")
        val body: String?,

        @SerialName("parameters")
        val parameters: Map<String, List<String>>

    )

}

/**
 *     "ddsource": "nginx",
 *     "ddtags": "env:staging,version:5.1",
 *     "hostname": "i-012345678",
 *     "message": "2019-11-19T14:37:58,995 INFO [process.name][20081] Hello World",
 *     "service": "payment"
 */