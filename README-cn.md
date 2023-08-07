<div align="center">
<h1>CutePixelsEvent</h1>

<p>CutePixelsEvent是一个开源且易于使用的Kotlin事件总线实现，它允许您使用发布-订阅模式高效地在应用程序的不同组件之间进行通信。</p>

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

## 特点
- 轻量、易于安装
- 简单直观的API
- 
## 安装
要在您的项目中使用CutePixelsEvent，您可以将依赖和我们的Maven Repo插入在Gradle构建文件中。

```groovy
repositories {
    maven {
        name = 'CutePixels Projects'
        url = 'https://raw.githubusercontent.com/CutePixels/mvn-repo/main'
    }
}
dependencies {
    implementation('cute.pixels:event:{版本号}')
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
    implementation('cute.pixels:event:{版本号}')
}
```
## 使用方法
获取事件总线对象：

```kotlin
val bus = cute.pixels.event.eventBus
```
订阅(监听)事件：

```kotlin
import cute.pixels.event.*

object Listeners {
    // 在任何位置监听（除了不在一个对象里的内种，你可以在Java中注册:>）
    @EventHandler
    fun onEvent(event: MyEvent) {
        // 处理事件
    }

}
```
注册事件监听器：

```kotlin
fun register() {
    // 注册一个监听器
    bus.registerListener(Listeners, "onEvent", MyEvent::class.java)
    // 注册一个对象中的所有监听器
    bus.registerListeners(Listeners)
}
```
创建事件：

```kotlin
import cute.pixels.event.*
class MyEvent : Event() {
    fun getMessage(): String {
        return "xxx"
    }
}
```
创建可取消的事件（如果被取消，其他监听器将不会执行）：

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
发布事件：

```kotlin
fun postEvents() {
eventBus.post(MyCancellableEvent)
eventBus.post(MyEvent)
}
```

## 致谢
- Guang_Chen_ ([GitHub](https://github.com/GuangChen2333)) 的 [CrystalKillListener](https://github.com/GuangChen2333/CrystalKillListener)

## 许可证
CutePixelsEvent在GPL 3.0许可下发布。有关更多信息，请参阅LICENSE文件。