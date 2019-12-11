package backend

import communication.Communication
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

object Backend : AbstractDevice(-1), InternetDevice {
    private const val port: Int = 20000
    override val address: SocketAddress = InetSocketAddress(port)
    override var communication = SocketCommunication(this)

    /**
     * Set of devices with their neighbours
     */
    private var members: MutableMap<Device, Set<Device>> = mutableMapOf()

    val devices: Set<Device>
    get() = members.keys

    private fun Device.neighbours(): Set<Device> = members.getOrDefault(this, emptySet())
    fun getNeighbours(device: Device) = device.neighbours()
    fun getNeighbours(id: Int) = getNeighbours(devices.single { it.id == id })

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
        devices.forEach(Device::execute)
    }

    override fun tell(message: Message) {

    }

    @JvmStatic
    fun main(args: Array<String>) {
        communication.startServer {
            val address = it.remoteAddress
            val message = communication.getMessage(it)
            when (message.type) {
                MessageType.Join -> {
                    println("$address wants to join")
                    val ip = address.toString().trim('/').split(':').first()
                    val port = message.content.toString().toInt()
                    val joining = RemoteDevice(generateID(), InetSocketAddress(InetAddress.getByName(ip), port))
                    subscribe(joining)
                    joining.tell(Message(id, MessageType.Result, joining.id))
                }
                else -> receivedMessages.add(communication.getMessage(it))
            }
        }
        while (true) {

        }
    }
}