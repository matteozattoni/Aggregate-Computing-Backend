package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import java.net.SocketAddress

/**
 * Device model that executes locally but shows the results on the physical device
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress) : EmulatedDevice(id), InternetDevice {
    override val physicalDevice = SocketCommunication(this)

    override fun execute() = adapter!!.execute()

    override fun showResult(result: String) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        if (message.type == MessageType.Result)
            physicalDevice.send(message)
    }

    fun goFullWeight() = RemoteDevice(id, address)
}