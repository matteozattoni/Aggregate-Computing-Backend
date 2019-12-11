package communication

import devices.Device
import devices.InternetDevice
import java.net.InetAddress
import java.net.SocketAddress

/**
 * Represents the way a Device model has to communicate with the physical one
 */
interface Communication<T> {
    val device: Device

    /**
     * Obtains the Message sent to this device
     */
    fun extractMessage(received: T) : Message

    fun startServer(onReceive: (T) -> Unit)
    fun stopServer()

    /**
     * Sends a Message to the physical Device
     */
    fun send(message: Message)
}