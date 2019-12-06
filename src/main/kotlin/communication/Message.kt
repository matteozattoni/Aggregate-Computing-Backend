package communication

import devices.PhysicalDevice
import java.io.Serializable

enum class MessageType { ReadSensor, Execute, Result }

data class Message(val senderUid: Int, val type: MessageType, val content: Any? = null) : Serializable