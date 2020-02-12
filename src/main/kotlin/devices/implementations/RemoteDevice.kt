package devices.implementations

import com.sun.org.apache.xpath.internal.operations.Bool
import communication.Message
import communication.MessageType
import communication.SocketCommunication
import devices.interfaces.AbstractDevice
import devices.interfaces.InternetDevice
import server.Support
import java.io.Serializable
import java.net.SocketAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, override val address: SocketAddress, name: String = "") :
        AbstractDevice(id, name, ::println), InternetDevice {

    override val physicalDevice = SocketCommunication(this)

    override fun showResult(result: Serializable) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when (message.type) {
            MessageType.Execute -> { /* Execute is automatically sent to the Device */ }
            MessageType.GoLightWeight -> {
                //the content is true only on Clients
                if (message.content as Boolean)
                    physicalDevice.send(message)
                else
                    goLightWeight()
            }
            else -> physicalDevice.send(message)
        }
    }

    override fun execute() = physicalDevice.send(Message(id, MessageType.Execute))

    private fun goLightWeight() {
        Support.devices.replace(this, LocalExecutionDevice(id, address, name))
        println("$id went lightweight")
    }
}