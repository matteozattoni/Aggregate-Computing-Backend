package adapters

import devices.Device

/**
 * Provides platform-specific operations
 */
interface Adapter {
    val device: Device

    fun execute(): Unit
}