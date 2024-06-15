package worker

import client
import io.dot.workmanager.work.Data
import io.dot.workmanager.work.WorkLorraine
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GetWorker : WorkLorraine() {

    override suspend fun doWork(inputData: Data?) {
        runCatching {
            client.get("https://dummyjson.com/test") {
                contentType(ContentType.Application.Json)
            }
        }
            .onFailure { it.printStackTrace() }
    }
}