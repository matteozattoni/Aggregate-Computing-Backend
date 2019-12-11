package devices

import communication.Communication
import communication.Message
import incarnations.Incarnation

abstract class AbstractDevice(override val id: Int) : Device {
    override var receivedMessages: MutableSet<Message> = mutableSetOf()

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