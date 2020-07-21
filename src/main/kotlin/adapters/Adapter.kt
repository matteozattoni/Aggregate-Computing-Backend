package adapters

import devices.interfaces.Device

/**
 * Provides platform-specific operations
 */
interface Adapter {
    val device: Device

    fun execute()
}