package communication

import devices.PhysicalDevice
import java.net.InetAddress

interface Communication {
    val address: InetAddress
    val port: Int

    var received: MutableSet<Message>

    fun start(onReceive: (Message) -> Unit)
    fun stop()

    fun send(message: Message, address: InetAddress, port: Int)
}