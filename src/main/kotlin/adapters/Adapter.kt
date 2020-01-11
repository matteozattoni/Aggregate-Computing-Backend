package adapters

import devices.Device

/**
 * Provides platform-specific operations
 */
interface Adapter {
    val device: Device

    fun readSensor(name: String): Any

    fun execute(): Unit
}