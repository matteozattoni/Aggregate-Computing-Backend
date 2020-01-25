package devices.client

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import devices.AbstractDevice
import devices.InternetDevice
import server.Support
import java.net.InetAddress
import java.net.InetSocketAddress

/**
 * Model of Server to be used by Clients
 */
class Server(address: InetAddress, port: Int) : AbstractDevice(Support.id, "Support"), InternetDevice {
    override val address = InetSocketAddress(address, port)
    override val physicalDevice = SocketCommunication(this)

    override fun execute() {
        //unused
    }

    override fun showResult(result: String) {
        //unused
    }

    override fun tell(message: Message) = physicalDevice.send(message)
}