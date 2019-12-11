package adapters

/**
 * Provides platform-specific operations
 */
interface Adapter {
    fun readSensor(name: String): Any

    fun execute(): Unit
}