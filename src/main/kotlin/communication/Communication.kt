package communication

import devices.PhysicalDevice
import java.net.InetAddress

interface Communication {
    val device: PhysicalDevice

    var received: MutableSet<Message>

    fun start(onReceive: (Message) -> Unit)
    fun stop()

    fun send(message: Message, address: InetAddress, port: Int)
    fun sendToDevice(message: Message) = send(message, device.address, device.port)
}