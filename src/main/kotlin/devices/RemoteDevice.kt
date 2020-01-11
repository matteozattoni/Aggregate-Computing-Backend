package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import java.net.SocketAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, override val address: SocketAddress) : AbstractDevice(id), InternetDevice {
    override val physicalDevice = SocketCommunication(this)

    override fun execute() = physicalDevice.send(Message(id, MessageType.Execute))

    /**
     * The Status is maintained both here in the model and sent to the physical device
     * Maintaining the status in the model is useful to avoid unnecessary communications
     */
    override fun tell(message: Message) {
        super.tell(message)
        when(message.type){
            MessageType.Status, MessageType.ID -> { physicalDevice.send(message) }
            else -> { }
        }
    }

    fun goLightWeight(a: Adapter) = LocalExecutionDevice(id, address).apply { adapter = a }
}