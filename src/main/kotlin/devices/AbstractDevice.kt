package devices

import communication.Communication

abstract class AbstractDevice(override val id: Int,
                              override val communication: Communication) : Device {

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