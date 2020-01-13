package devices

import communication.Message
import communication.MessageType

abstract class AbstractDevice(override val id: Int) : Device {
    override var status: MutableSet<Message> = mutableSetOf()

    override fun tell(message: Message) {
        when(message.type){
            MessageType.Status -> status.add(message)
            MessageType.Result -> {
                //remove old results from same device
                status.removeAll { it.senderUid == message.senderUid }
                //add the new result
                status.add(message)
            }
            MessageType.Execute -> execute()
            else -> { }
        }
    }

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