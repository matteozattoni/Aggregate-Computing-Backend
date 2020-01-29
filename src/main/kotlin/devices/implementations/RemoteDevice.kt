package devices.implementations

import communication.Message
import communication.MessageType
import adapters.Adapter
import communication.SocketCommunication
import devices.interfaces.AbstractDevice
import devices.interfaces.InternetDevice
import server.Support
import java.net.SocketAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, override val address: SocketAddress, name: String = "") :
        AbstractDevice(id, name, ::println), InternetDevice {

    override val physicalDevice = SocketCommunication(this)

    override fun showResult(result: String) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when (message.type) {
            MessageType.Result, MessageType.ID -> physicalDevice.send(message)
            else -> { }
        }
    }

    override fun execute() = physicalDevice.send(Message(id, MessageType.Execute))

    fun goLightWeight(adapterBuilder: (LocalExecutionDevice) -> Adapter) =
        Support.devices.replace(this, LocalExecutionDevice(id, address, name, adapterBuilder))
}