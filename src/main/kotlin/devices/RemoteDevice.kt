package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import server.Support
import java.net.SocketAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, override val address: SocketAddress, name: String = "") : AbstractDevice(id, name), InternetDevice {
    override val physicalDevice = SocketCommunication(this)

    override fun execute() = physicalDevice.send(Message(id, MessageType.Execute))

    override fun showResult(result: String) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when(message.type){
            //MessageType.Status, MessageType.ID -> { physicalDevice.send(message) }
            else -> { physicalDevice.send(message) }
        }
    }

    fun goLightWeight(a: Adapter) = Support.devices.replace(this, LocalExecutionDevice(id, address, name) { a } )
}