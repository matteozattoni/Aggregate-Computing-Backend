package devices

import communication.Communication
import incarnations.Incarnation

abstract class AbstractDevice(override val id: Int) : Device {

    override lateinit var communication: Communication

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is AbstractDevice -> id == other.id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id
    }
}