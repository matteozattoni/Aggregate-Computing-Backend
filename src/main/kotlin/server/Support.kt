package server

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import devices.AbstractDevice
import devices.Device
import devices.InternetDevice
import devices.RemoteDevice
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.AsynchronousSocketChannel

/**
 *
 */
object Support : AbstractDevice(-1), InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), port)
    override var physicalDevice = SocketCommunication(this)

    val devices: DeviceManager = DeviceManager();

    override fun execute() {
        //devices.finalizeIfNecessary()
        devices.getDevices().forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {
    }

    override fun showResult(result: String) {
    }

    private val defaultSocketCallback: (AsynchronousSocketChannel) -> Unit = {
        println("received something")
        val address = it.remoteAddress
        val message = physicalDevice.extractMessage(it)
        when (message.type) {
            MessageType.Join -> {
                val ip = address.toString().trim('/').split(':').first()
                val port = message.content.toString().toInt()
                println("$ip wants to join at port $port")
                val joining = devices.createAndAddDevice { id ->
                    RemoteDevice(id, InetSocketAddress(InetAddress.getByName(ip), port))
                }
                joining.tell(Message(id, MessageType.ID, joining.id))
            }
            MessageType.SendToNeighbours -> devices.getNeighbours(message.senderUid).forEach { n ->
                n.tell(message.content as Message)
            }
            else -> { }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        physicalDevice.startServer(defaultSocketCallback)
        while (true) {

        }
    }
}