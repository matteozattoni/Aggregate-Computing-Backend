package devices

import communication.Communication
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress

/**
 * Fully emulated Device
 */
class EmulatedDevice(id: Int,
                     incarnation: Incarnation,
                     override val port: Int,
                     communication: Communication = SocketCommunication(InetAddress.getLocalHost(), port)
) : IncarnatedDevice(id, incarnation, communication), PhysicalDevice {
    override val address: InetAddress = communication.address
}