package devices.server

import communication.Message
import communication.MessageType
import adapters.Adapter
import communication.SocketCommunication
import devices.AbstractDevice
import devices.InternetDevice
import server.Support
import java.net.SocketAddress

/**
 * Device model that executes locally but shows the results on the physical device
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress, name: String,
                           adapterBuilder: (LocalExecutionDevice) -> Adapter) :
        AbstractDevice(id, name, ::println), InternetDevice {

    override val physicalDevice = SocketCommunication(this)

    override fun showResult(result: String) {
        physicalDevice.send(Message(id, MessageType.Result, result))
    }

    var adapter: Adapter = adapterBuilder(this)

    override fun execute() = adapter.execute()

    override fun tell(message: Message) {
        super.tell(message)
        if (message.type == MessageType.Result)
            physicalDevice.send(message)
    }

    fun goFullWeight() = Support.devices.replace(this, RemoteDevice(id, address))
}