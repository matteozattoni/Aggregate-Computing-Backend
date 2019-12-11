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
    override val communication = SocketCommunication(this)

    override fun execute() = communication.send(Message(id, MessageType.Execute))

    override fun tell(message: Message) = communication.send(message)

    fun goLightWeight(adapterBuilder: (LocalExecutionDevice) -> Adapter) = LocalExecutionDevice(id, address, adapterBuilder)
}