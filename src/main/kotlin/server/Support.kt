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
    override val address: SocketAddress = InetSocketAddress(port)
    override var physicalDevice = SocketCommunication(this)

    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    val devices: Set<Device>
    get() = members.keys

    private fun Device.neighbours(): Set<Device> = members.getOrDefault(this, emptySet())
    fun getNeighbours(device: Device) = device.neighbours()
    fun getNeighbours(id: Int) = getNeighbours(devices.single { it.id == id })

    /**
     * Generates a new, unused Device ID
     */
    private fun generateID(): Int {
        var generated = devices.size
        while (devices.any { it.id == generated })
            generated++
        return generated
    }

    fun subscribe(device: Device, neighbours: Set<Device> = emptySet()) {
        members.putIfAbsent(device, neighbours)
    }

    override fun execute() {
        devices.forEach { it.tell(Message(id, MessageType.Execute))}
    }

    override fun tell(message: Message) {

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
                val joining = RemoteDevice(generateID(), InetSocketAddress(InetAddress.getByName(ip), port))
                subscribe(joining)
                joining.tell(Message(id, MessageType.ID, joining.id))
            }
            MessageType.SendToNeighbours -> getNeighbours(message.senderUid).forEach { n ->
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