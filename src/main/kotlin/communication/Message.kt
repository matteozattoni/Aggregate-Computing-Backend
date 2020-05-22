package communication

import org.protelis.lang.datatype.DeviceUID
import java.io.Serializable

enum class MessageType : Serializable { Execute, Result, Show, Join, OfferServer, ID, Status, SendToNeighbours, GoLightWeight, LeaveLightWeight }

data class Message(val senderUid: DeviceUID, val type: MessageType, val content: Serializable? = null) : Serializable