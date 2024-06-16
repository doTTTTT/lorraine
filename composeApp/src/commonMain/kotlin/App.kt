import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.dot.lorraine.dsl.lorraine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.LogBody
import ui.TestScreen
import ui.TestViewModel
import worker.DeleteWorker
import worker.GetWorker
import worker.PatchWorker
import worker.PostWorker
import worker.PutWorker

const val GET_WORKER = "GET_WORKER"
const val POST_WORKER = "DELETE_WORKER"
const val PUT_WORKER = "PUT_WORKER"
const val PATCH_WORKER = "PATCH_WORKER"
const val DELETE_WORKER = "DELETE_WORKER"

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = remember { TestViewModel() }

        lorraine {
            work(GET_WORKER) { GetWorker() }
            work(POST_WORKER) { PostWorker() }
            work(PATCH_WORKER) { PatchWorker() }
            work(PUT_WORKER) { PutWorker() }
            work(DELETE_WORKER) { DeleteWorker() }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TestScreen(viewModel)
        }
    }
}


private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}
private val logClient = HttpClient(engine()) {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("LOG: $message")
            }
        }
    }
    install(ContentNegotiation) {
        json(json)
    }
}
val client = HttpClient(engine()) {
    expectSuccess = true

    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("Test: $message")
            }
        }
    }

    install(ContentNegotiation) {
        json(json)
    }

    ResponseObserver {
        runCatching {
            logClient.post("https://http-intake.logs.datadoghq.eu/api/v2/logs") {
                contentType(ContentType.Application.Json)
                header("DD-API-KEY", "pub2a2c9638ba0e802b5350394e3ab298fc")
                setBody(
                    listOf(
                        LogBody(
                            ddSource = getPlatform().name,
                            ddTags = "lorraine",
                            hostname = "dummy.com",
                            service = "lorraine-kmp",
                            message = json.encodeToString(it.createLog()),
                        )
                    )
                )
            }
        }
            .onFailure(Throwable::printStackTrace)
    }
}


private suspend fun HttpResponse.createLog() = LogBody.Log(
    statusCode = status.value,
    method = request.method.value,
    request = LogBody.Body(
        headers = request.headers.toMap(),
        body = request.content.toString(),
        parameters = request.url.parameters.toMap()
    ),
    response = LogBody.Body(
        headers = headers.toMap(),
        body = json.encodeToString(call.body<JsonElement>()),
        parameters = emptyMap()
    )
)