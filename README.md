### THIS IS A WORK IN PROGRESS

# Lorraine

Lorraine is a work management framework for tasks.

### Get started

First add the dependency to your project:

```toml
[versions]
lorraine = "0.0.1"

[libraries]
lorraine = { module = "io.github.dottttt.lorraine:lorraine", version.ref = "lorraine" }
```

## Using it

#### Initialization

```kotlin
const val GET_WORKER = "GET_WORKER"

lorraine {
    work(GET_WORKER) { GetWorker() }
    ...
}
```

```kotlin
class GetWorker : WorkLorraine() {

    override suspend fun doWork(inputData: Data?): LorraineResult {
        ...
        return LorraineResult.success() / LorraineResult.retry() / LorraineResult.failure()
    }
    
}
```

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

## Principles

## Documentation



## Reporting Issues / Support

Report any issue on this GitHub repository.

## Inspirations

- Koin: https://github.com/InsertKoinIO/koin

- Android: WorkManager
- iOS: NSOperation

## Contributing

[//]: # (Please see [the contribution guide]&#40;CONTRIBUTING.md&#41; and the [Code of conduct]&#40;CODE_OF_CONDUCT.md&#41; before contributing.)
