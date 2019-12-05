package communication

import devices.PhysicalDevice
import java.io.Serializable

data class Message(val senderUid: Int, val content: Any) : Serializable