/*
 * CutePixels on https://cutepixels.github.io
 * This project open source forever.
 * Created by https://github.com/rocky-co at 2023-07-30 10:34
*/
package cute.pixels.event

import java.lang.reflect.Method

/**
 * like bukkit EventHandler.
 * @see org.bukkit.event.EventHandler
 *
 * You can add this annotation to a method and register it to create a listener! In CutePixelsEvent, you don't have to implement the Listener interface!
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EventHandler(
    val prio: EventPrio = EventPrio.NORMAL
)

/**
 * like bukkit EventPriority.
 * @see org.bukkit.event.EventPriority
 * @see EventHandler
 * @property value prio level num
 *
 * Add this to EventHandler annotation arguments!
 */
enum class EventPrio(val value: Int) {
    MEDIUM(0),
    NORMAL(-1),
    PRO(-2),
    PLUS(-3),
    GOOD(-4),
    MAX(-5)
}

/**
 * like bukkit Cancellable
 * @see org.bukkit.event.Cancellable
 * @see EventHandler
 * @see EventPrio
 * @property isCancelled can set cancelled
 *
 * You can remove the listening of other Listeners in a Listener! It works even better with <b>EventHandler</b> and <b>EventPrio</b>!
 *
 */
interface Cancellable {
    var isCancelled: Boolean
}



abstract class Event

val eventBus=EventBus()

/**
 * can register some listeners and post events
 *
 * 参考
 *
 * Bukkit org.bukkit.Bukkit
 *
 */
class EventBus {
    private val listeners: MutableMap<Class<*>, MutableList<ListenerWrapper>> = HashMap()

    /**
     * Register all listeners in a object.
     *
     * @param listener an object. the function will register all listeners in this object.
     */
    fun registerListeners(listener: Any) {
        val listenerClass = listener.javaClass
        val methods = listenerClass.declaredMethods

        for (method in methods) {
            val eventHandlerAnnotation = method.getAnnotation(EventHandler::class.java)
            if (eventHandlerAnnotation != null) {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size == 1) {
                    val eventType = parameterTypes[0]
                    val wrapper = ListenerWrapper(listener, method, eventType, eventHandlerAnnotation.prio)
                    registerListener(eventType, wrapper)
                }
            }
        }
    }

    /**
     * Register a listener in a object.
     *
     * @param listener is the object where the Listener function resides.
     * @param methodName Listener function name.
     * @param eventType listen type (listener param)
     */
    fun registerListener(listener: Any, methodName: String, eventType: Class<*>) {
        try {
            val method = listener.javaClass.getDeclaredMethod(methodName, eventType)
            val eventHandlerAnnotation = method.getAnnotation(EventHandler::class.java)
            val prio=eventHandlerAnnotation.prio
            val wrapper = ListenerWrapper(listener, method, eventType, prio)
            registerListener(eventType, wrapper)
        } catch (exception: NoSuchMethodException) {
            println("Method $methodName with parameter type $eventType not found in ${listener.javaClass.simpleName}")
        }
    }

    /**
     * when you execute this function, all listener of listening this event was executed.
     *
     * @param event the event
     */
    fun post(event: Any) {
        val eventType = event.javaClass

        if (listeners.containsKey(eventType)) {
            val wrappers = listeners[eventType]

            for (wrapper in wrappers.orEmpty()) {
                if (wrapper.method.parameterCount == 1) {
                    val parameterType = wrapper.method.parameterTypes[0]
                    if (parameterType.isAssignableFrom(eventType)) {
                        val cancellable = event as? Cancellable
                        if (cancellable == null || !cancellable.isCancelled) {
                            wrapper.method.invoke(wrapper.listener, event)
                        }
                    }
                }
            }
        }
    }

    /**
     * If you don't want certain listeners to take effect, you can execute this function.
     *
     * @param the listener object
     */
    fun unregister(listener: Any) {
        val listenerClass = listener.javaClass
        val wrappers = listeners.values.flatten()

        for (wrapper in wrappers) {
            if (wrapper.listener.javaClass == listenerClass) {
                listeners[wrapper.eventType]?.remove(wrapper)
            }
        }
    }

    /**
     * this is a private function.
     * @param eventType type of event
     * @param wrapper listener data
     */
    private fun registerListener(eventType: Class<*>, wrapper: ListenerWrapper) {
        if (wrapper.method.parameterCount != 1) {
            throw ListenerParameterSizeException("Parameter size of listener method ${wrapper.method.name} in class ${wrapper.listener.javaClass.simpleName} should be 1. 监听器方法 ${wrapper.listener.javaClass.simpleName} 的 ${wrapper.method.name} 方法参数数量应为 1.")
        } else if (!Event::class.java.isAssignableFrom(wrapper.method.parameterTypes[0])) {
            throw ListenerParameterTypeException("Parameter type of listener method ${wrapper.method.name} in class ${wrapper.listener.javaClass.simpleName} should be a subclass of Event. 监听器方法 ${wrapper.listener.javaClass.simpleName} 的 ${wrapper.method.name} 方法参数类型应为 Event 及其子类.")
        }
        if (!listeners.containsKey(eventType)) {
            listeners[eventType] = mutableListOf()
        }
        listeners[eventType]?.add(wrapper)
        listeners[eventType]?.sortWith(compareByDescending { it.prio.value })
    }

    /**
     * @property listener listener object
     * @property method listener method
     * @property eventType listening event type of listener
     * @property prio event prio level
     */
    private data class ListenerWrapper(
        val listener: Any,
        val method: Method,
        val eventType: Class<*>,
        val prio: EventPrio
    )


}

/**
 * @property message english
 */
class ListenerParameterSizeException(message: String) : Exception("ListenerParameterSizeException: $message\n监听器参数数量不正确。")

/**
 * @property message english
 */
class ListenerParameterTypeException(message: String) : Exception("ListenerParameterTypeException: $message\n监听器参数类型不正确。")
