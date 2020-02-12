package server

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import devices.implementations.RemoteDevice
import devices.interfaces.AbstractDevice
import devices.interfaces.InternetDevice
import java.io.Serializable
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress

const val SUPPORT_ID = -1

/**
 * The server basis.
 * The only place where the net topology is known.
 */
object Support : AbstractDevice(SUPPORT_ID, "Support", ::println),
    InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), port)
    override var physicalDevice = SocketCommunication(this)

    val devices: DeviceManager = DeviceManager()

    override fun execute() {
        devices.getDevices().forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {
        when (message.type) {
            MessageType.Join -> {
                val joiningAddress = message.content as SocketAddress
                val joining = devices.createAndAddDevice { id -> RemoteDevice(id, joiningAddress) }
                //tell to the physical device the assigned ID
                joining.tell(Message(Support.id, MessageType.ID, joining.id))

                println("$joiningAddress joined with id ${joining.id}")
            }
            MessageType.SendToNeighbours -> devices.getNeighbours(message.senderUid).forEach { it.tell(message.content as Message) }
            MessageType.GoLightWeight,
            MessageType.LeaveLightWeight -> devices.getDevices().single { it.id == message.senderUid }.tell(message)
            else -> { }
        }
    }

    override fun showResult(result: Serializable) {
        //unused
    }
}