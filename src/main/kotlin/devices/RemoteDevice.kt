package devices

import communication.Communication
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, incarnation: Incarnation,
                   override val port: Int,
                   override val address: InetAddress = InetAddress.getLocalHost(),
                   communication: Communication = SocketCommunication(address, port)
) : IncarnatedDevice(id, incarnation, communication), PhysicalDevice {

    companion object {
        fun createFromLocalExecution(local: LocalExecutionDevice): RemoteDevice =
            RemoteDevice(local.id, local.incarnation, local.port, local.address)
    }
}