package devices.implementations

import communication.Message
import communication.MessageType
import adapters.Adapter
import communication.SocketCommunication
import devices.interfaces.AbstractDevice
import devices.interfaces.EmulatedDevice
import devices.interfaces.InternetDevice
import server.Support
import java.io.Serializable
import java.net.SocketAddress

/**
 * Device model that executes locally but shows the results on the physical device
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress, name: String,
                           adapterBuilder: (EmulatedDevice) -> Adapter) :
        EmulatedDevice(id, name, adapterBuilder, ::println), InternetDevice {

    override val physicalDevice = SocketCommunication(this)

    override fun showResult(result: Serializable) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    override fun tell(message: Message) {
        super.tell(message)
        when (message.type) {
            MessageType.Result, MessageType.Show -> physicalDevice.send(message)
            MessageType.LeaveLightWeight -> goFullWeight()
            else -> { }
        }
    }

    private fun goFullWeight() = Support.devices.replace(this, RemoteDevice(id, address, name))
}