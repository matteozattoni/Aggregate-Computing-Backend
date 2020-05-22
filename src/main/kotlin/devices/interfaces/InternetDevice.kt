package devices.interfaces

import communication.interfaces.Communication
import java.net.SocketAddress

/**
 * A device model that needs to communicate with a physical counterpart through Internet
 */
interface InternetDevice : Device {
    val address: SocketAddress

    val physicalDevice: Communication<*>
}