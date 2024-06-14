import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect fun engine(): HttpClientEngineFactory<HttpClientEngineConfig>