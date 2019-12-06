package devices

import communication.Communication
import incarnations.Incarnation

/**
 * Device model that uses an Incarnation
 */
abstract class IncarnatedDevice(id: Int): AbstractDevice(id), PhysicalDevice {

    lateinit var incarnation: Incarnation

    fun initialize(incarnation: Incarnation, communication: Communication) {
        this.communication = communication
        this.incarnation = incarnation
    }

    abstract fun getSensor(sensorName: String): Any
}