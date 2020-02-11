package devices.interfaces

import communication.Message
import communication.MessageType
import scala.Tuple2
import scala.collection.immutable.List

abstract class AbstractDevice(override val id: Int,
                              override val name: String,
                              private val onResult: (String) -> Any) : Device {
    override var status: MutableSet<Message> = mutableSetOf()

    override fun tell(message: Message) {
        when(message.type){
            MessageType.Status,
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
            is AbstractDevice -> id == other.id && this.javaClass == other.javaClass
            else -> super.equals(other)
        }
    }

    override fun showResult(result: String) { onResult(result) }

    /**
     * Shows the name of this device
     * If unspecified, the id is usd instead
     */
    override fun toString(): String = if (name.isNotEmpty()) name else id.toString()

    override fun hashCode(): Int {
        return id
    }
}