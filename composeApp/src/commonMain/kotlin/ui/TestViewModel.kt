package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engine
import getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class TestViewModel : ViewModel() {

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
    private val client = HttpClient(engine()) {
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

    private val _uiState = MutableStateFlow(TestUIState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TestEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: TestAction) {
        when (action) {
            is TestAction.Send -> send(action)
        }
    }

    private fun send(action: TestAction.Send) {
        viewModelScope.launch {
            runCatching {
                when (action.methodType) {
                    MethodType.GET -> client.get("https://dummyjson.com/test") {
                        contentType(ContentType.Application.Json)
                    }

                    MethodType.POST -> TODO()
                    MethodType.PUT -> TODO()
                    MethodType.PATCH -> TODO()
                    MethodType.DELETE -> TODO()
                }
            }
                .onFailure { it.printStackTrace() }
                .onSuccess { sendEvent(TestEvent.Message(action.methodType.name)) }
        }
    }

    private fun sendEvent(event: TestEvent) {
        viewModelScope.launch {
            _event.emit(event)
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

}