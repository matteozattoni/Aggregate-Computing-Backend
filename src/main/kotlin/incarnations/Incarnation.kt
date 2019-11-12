package incarnations

/**
 * Provides platform-specific operations
 */
interface Incarnation {
    fun readSensor(name: String): Any

}