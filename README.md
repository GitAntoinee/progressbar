# progressbar
Terminal-based progressbar for Kotlin multiplatform

## Preview
![JVM](preview.gif)

## Targets
For the moment, only JVM is available. Because coroutines are only available on the main thread in Kotlin Native.

## Installation
The library is available on jcenter and jitpack. JCenter provides only stable releases whereas jitpack provide releases and snapshots.

```kotlin
repositories {
    jcenter() // Releases only
    maven("https://jitpack.io") // Releases and snapshots
}
```

## Usage
The usage is very straightforward

```kotlin
ProgressBar(ticks = 0, maxTicks = 100).use { progressBar -> // ProgressBar.close() interrupt the threads
    progressBar.tick() // Increment ticks by 1
    progressBar.tickBy(10) // Increment ticks by 10
    progressBar.tickTo(100) // Set ticks to 100
}
```
