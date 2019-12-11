package devices

import communication.Communication
import communication.Message

interface Device {
    val id: Int

    fun execute()

    var receivedMessages: MutableSet<Message>
    fun tell(message: Message)
}