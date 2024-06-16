package worker

import client
import io.dot.lorraine.work.Data
import io.dot.lorraine.work.LorraineResult
import io.dot.lorraine.work.WorkLorraine
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PutWorker : WorkLorraine() {

    override suspend fun doWork(inputData: Data?): LorraineResult {
        return runCatching {
            client.put("https://dummyjson.com/test") {
                contentType(ContentType.Application.Json)
            }
        }
            .onFailure { it.printStackTrace() }
            .fold(
                onSuccess = { LorraineResult.success() },
                onFailure = { LorraineResult.failure() }
            )
    }
}