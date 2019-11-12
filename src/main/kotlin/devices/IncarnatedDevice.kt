package devices

import incarnations.Incarnation

abstract class IncarnatedDevice(id: Int, private val incarnation: Incarnation): AbstractDevice(id) {
    override fun getSensor(sensorName: String): Any = incarnation.readSensor(sensorName)

}