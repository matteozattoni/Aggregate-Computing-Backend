package devices

import adapters.Adapter
import communication.Message

/**
 * Emulated Device model, it needs an Adapter to be able to execute
 */
abstract class EmulatedDevice(id: Int): AbstractDevice(id) {
    abstract val adapter: Adapter

    abstract fun getSensor(sensorName: String): Any
}