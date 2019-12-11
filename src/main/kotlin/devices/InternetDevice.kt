package devices

import communication.Communication
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress

/**
 * A device model that needs to communicate with a physical counterpart through Internet
 */
interface InternetDevice : Device {
    val address: SocketAddress

    val communication: Communication<*>
}