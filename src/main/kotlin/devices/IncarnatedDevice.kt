package devices

import communication.Communication
import incarnations.Incarnation

/**
 * Device model that uses an Incarnation
 */
abstract class IncarnatedDevice(id: Int): AbstractDevice(id) {

    abstract val incarnation: Incarnation

    abstract fun getSensor(sensorName: String): Any
}