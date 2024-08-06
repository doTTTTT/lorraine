package worker

import client
import io.dot.lorraine.work.Data
import io.dot.lorraine.work.LorraineResult
import io.dot.lorraine.work.WorkLorraine
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class PostWorker : WorkLorraine() {

    override suspend fun doWork(inputData: Data?): LorraineResult {
        return runCatching {
            client.post("https://dummyjson.com/test") {
                contentType(ContentType.Application.Json)
                setBody(JsonObject(mapOf("test" to JsonPrimitive("value"))))
            }
        }
            .onFailure { it.printStackTrace() }
            .fold(
                onSuccess = { LorraineResult.success() },
                onFailure = { LorraineResult.failure() }
            )
    }
}
