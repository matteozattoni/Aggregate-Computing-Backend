package communication

import devices.Device
import devices.InternetDevice
import java.net.InetAddress
import java.net.SocketAddress

interface Communication<T> {
    val device: Device

    fun getMessage(received: T) : Message

    fun startServer(onReceive: (T) -> Unit)
    fun stop()

    fun send(message: Message)
}