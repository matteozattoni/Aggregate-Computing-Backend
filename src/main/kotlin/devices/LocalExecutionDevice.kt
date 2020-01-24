package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import server.Support
import java.net.SocketAddress

/**
 * Device model that executes locally but shows the results on the physical device
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress, name: String, adapterBuilder: (EmulatedDevice) -> Adapter) : EmulatedDevice(id, name, adapterBuilder), InternetDevice {
    override val physicalDevice = SocketCommunication(this)

    override fun showResult(result: String) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        if (message.type == MessageType.Result)
            physicalDevice.send(message)
    }

    fun goFullWeight() = Support.devices.replace(this, RemoteDevice(id, address))
}