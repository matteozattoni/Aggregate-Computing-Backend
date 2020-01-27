package communication

import java.io.Serializable

enum class MessageType : Serializable { Execute, Result, Join, ID, Status, SendToNeighbours }

data class Message(val senderUid: Int, val type: MessageType, val content: Serializable? = null) : Serializable