[//]: # (<div align="center">)

[//]: # ()
[//]: # (  <picture>)

[//]: # (    <source media="&#40;prefers-color-scheme: dark&#41;" srcset="https://raw.githubusercontent.com/ktorio/ktor/main/.github/images/ktor-logo-for-dark.svg">)

[//]: # (    <img alt="Ktor logo" src="https://raw.githubusercontent.com/ktorio/ktor/main/.github/images/ktor-logo-for-light.svg">)

[//]: # (  </picture>)

[//]: # ()
[//]: # (</div>)

[//]: # ([![Official JetBrains project]&#40;http://jb.gg/badges/official.svg&#41;]&#40;https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub&#41;)
[//]: # ([![Maven Central]&#40;https://img.shields.io/maven-central/v/io.ktor/ktor&#41;]&#40;https://mvnrepository.com/artifact/io.ktor&#41;)
[//]: # ([![Kotlin]&#40;https://img.shields.io/badge/kotlin-1.8.22-blue.svg?logo=kotlin&#41;]&#40;http://kotlinlang.org&#41;)
[//]: # ([![Slack channel]&#40;https://img.shields.io/badge/chat-slack-green.svg?logo=slack&#41;]&#40;https://kotlinlang.slack.com/messages/ktor/&#41;)
[//]: # ([![GitHub License]&#40;https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat&#41;]&#40;http://www.apache.org/licenses/LICENSE-2.0&#41;)
[//]: # ([![Contribute with Gitpod]&#40;https://img.shields.io/badge/Contribute%20with-Gitpod-908a85?logo=gitpod&#41;]&#40;https://gitpod.io/#https://github.com/ktorio/ktor&#41;)

Lorraine is a work management framework for tasks.

First add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.dot.lorraine:lorraine:$lorraine_version")
}
```

## Start using Lorraine



## Principles

#### Testable

Ktor applications can be hosted in a special test environment, which emulates a web server to some
extent without actually doing any networking. It provides easy way to test an application without mocking
too much stuff, and still achieve good performance while validating application calls. Running integration tests with a
real
embedded web server are of course possible, too.

## Documentation

ADD DOCUMENTATION

## Reporting Issues / Support

Report any issue on this GitHub repository.

## Inspirations

- Android: WorkManager
- iOS: NSOperation

## Contributing

[//]: # (Please see [the contribution guide]&#40;CONTRIBUTING.md&#41; and the [Code of conduct]&#40;CODE_OF_CONDUCT.md&#41; before contributing.)