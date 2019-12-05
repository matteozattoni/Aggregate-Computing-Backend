package devices

import communication.Communication

interface Device {

    val id: Int
    val communication: Communication

    fun execute()

    fun getSensor(sensorName: String): Any

}