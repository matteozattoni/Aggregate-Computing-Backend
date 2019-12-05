package devices

import communication.Communication
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress

/**
 * Device model that executes locally but reads/writes sensors/actuators remotely
 */
class LocalExecutionDevice(id: Int, incarnation: Incarnation,
                           override val port: Int,
                           override val address: InetAddress = InetAddress.getLocalHost(),
                           communication: Communication = SocketCommunication(address, port)
) : IncarnatedDevice(id, incarnation, communication), PhysicalDevice {

    companion object {
        fun createFromRemote(remote: RemoteDevice): LocalExecutionDevice =
            LocalExecutionDevice(remote.id, remote.incarnation, remote.port, remote.address)
    }

}