package devices

import communication.Communication
import incarnations.Incarnation

abstract class IncarnatedDevice(id: Int,
                                val incarnation: Incarnation,
                                communication: Communication): AbstractDevice(id, communication) {
    override fun getSensor(sensorName: String): Any = incarnation.readSensor(sensorName)

    override fun execute() {
        incarnation.execute()
    }
}