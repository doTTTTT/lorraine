### THIS IS A WORK IN PROGRESS

Subject to change.

I'm having some issue publishing the library due to Room. Opened an issue on issuetracker, waiting. Still trying to fix it on my side.

# Lorraine

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.dottttt.lorraine/lorraine/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.dottttt.lorraine/lorraine)

Lorraine is a work management framework for tasks.

### Get started

First add the dependency to your project:

Lastest vesion: https://mvnrepository.com/artifact/io.github.dottttt.lorraine/lorraine

```toml
[versions]
lorraine = "$lastestversion"

[libraries]
lorraine = { module = "io.github.dottttt.lorraine:lorraine", version.ref = "lorraine" }
```

## Using it

#### Initialization

Initialize Lorraine with workers.

```kotlin
const val GET_WORKER = "GET_WORKER"

lorraine {
    work(GET_WORKER) { GetWorker() }
    ...
}
```

Create workers by extending `WorkLorraine`

```kotlin
class GetWorker : WorkLorraine() {

    override suspend fun doWork(inputData: Data?): LorraineResult {
        ...
        return LorraineResult.success() / LorraineResult.retry() / LorraineResult.failure()
    }
    
}
```

Launch your worker

```kotlin
Lorraine.enqueue(
    uniqueId = "UNIQUE_ID",
    type = ExistingLorrainePolicy.APPEND,
    request = lorraineRequest {
        identifier = GET_WORKER
        constraints { 
            requiredNetwork = true
        }
    }
)
```

## Reporting Issues / Support

Report any issue on this GitHub repository.

## Inspirations

- Koin (for dsl): https://github.com/InsertKoinIO/koin
- WorkManager (for Android): https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started
- NSOperation (for iOS): https://developer.apple.com/documentation/foundation/nsoperation

## Contributing

Feel free to open pull request
