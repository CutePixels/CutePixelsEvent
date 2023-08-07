<div align="center">
<h1>CutePixelsEvent</h1>


<p>CutePixelsEvent is an open source and easy-to-use Kotlin implementation of an event bus. It allows you to efficiently communicate between different components of your application using a publish-subscribe pattern.</p>

<p>
  <a href="https://github.com/CutePixels/CutePixelsEvent/blob/main/README-cn.md">
<img src="https://img.shields.io/badge/Documents-%E4%B8%AD%E6%96%87-blue" />
  </a>
</p>

<p>
  <a href="https://github.com/CutePixels/CutePixelsEvent/graphs/contributors">
    <img src="https://img.shields.io/github/contributors/CutePixels/CutePixelsEvent" alt="contributors" />
  </a>
  <a href="">
    <img src="https://img.shields.io/github/last-commit/CutePixels/CutePixelsEvent" alt="last update" />
  </a>
  <a href="https://github.com/CutePixels/CutePixelsEvent/network/members">
    <img src="https://img.shields.io/github/forks/CutePixels/CutePixelsEvent" alt="forks" />
  </a>
  <a href="https://github.com/CutePixels/CutePixelsEvent/stargazers">
    <img src="https://img.shields.io/github/stars/CutePixels/CutePixelsEvent" alt="stars" />
  </a>
  <a href="https://github.com/CutePixels/CutePixelsEvent/issues/">
    <img src="https://img.shields.io/github/issues/CutePixels/CutePixelsEvent" alt="open issues" />
  </a>
  <a href="https://github.com/CutePixels/CutePixelsEvente/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/CutePixels/CutePixelsEvent.svg" alt="license" />
  </a>
</p>
</div>

## Features
- Lightweight event bus
- Simple and intuitive API
- 
## Installation
To use CutePixelsEvent in your project, you can include it in your Gradle build file.

```groovy
repositories {
    maven {
        name = 'CutePixels Projects'
        url = 'https://raw.githubusercontent.com/CutePixels/mvn-repo/main'
    }
}
dependencies {
    implementation('cute.pixels:event:{VERSION}')
}
```
Kotlin DSL:

```kotlin
repositories {
    maven {
        name = 'CutePixels Projects'
        url = uri('https://raw.githubusercontent.com/CutePixels/mvn-repo/main')
    }
}
dependencies {
    implementation('cute.pixels:event:{VERSION}')
}
```

## Usage
Subscribe to events:

```kotlin
import cute.pixels.event.*

object Listeners {
    // Listen anywhere except inline functions
    @EventHandler
    fun onEvent(event: MyEvent) {
        // handle event
    }
    
}
```
Register Event Listener(s):

```kotlin
fun register() {
    // register a Listener
    EventBus.registerListener(Listeners, "onEvent", MyEvent::class.java)
    // register all Listener in a object
    EventBus.registerListeners(Listeners)
}
```
Create an event:

```kotlin
import cute.pixels.event.*
class MyEvent : Event() {
    fun getMessage(): String {
        return "xxx"
    }
}
```
Create a cancellable event(if cancelled, other listener will not executed):

```kotlin
import cute.pixels.event.*
class MyCancellableEvent : Event(), Cancellable {
    override var isCancelled: Boolean = false
        get() = field
        set(value) {field=value}
    fun getMessage(): String {
        return "xxx"
    }
}
```
Post an event:

```kotlin
fun postEvents() {
    EventBus.post(MyCancellableEvent)
    EventBus.post(MyEvent)
}
```

If you are using Java, you might need to append `.Companion` to `EventBus` when using the code.



## Thanks
- Guang_Chen_ ([GitHub](https://github.com/GuangChen2333))'s [CrystalKillListener](https://github.com/GuangChen2333/CrystalKillListener)

## License
CutePixelsEvent is released under the GPL 3.0 License. See LICENSE for more information.
