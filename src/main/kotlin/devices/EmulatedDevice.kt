package devices

import adapters.Adapter

/**
 * Emulated Device model, it needs an Incarnation to be able to execute
 */
abstract class EmulatedDevice(id: Int): AbstractDevice(id) {

    abstract val adapter: Adapter

    abstract fun getSensor(sensorName: String): Any
}