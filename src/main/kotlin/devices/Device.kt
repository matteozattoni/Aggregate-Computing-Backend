package devices

import communication.Communication

interface Device {

    val id: Int
    var communication: Communication

    fun execute()
}