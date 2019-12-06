package devices

import communication.Communication
import communication.Message
import communication.MessageType
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress

/**
 * Device model that executes locally but reads/writes sensors/actuators remotely
 */
class LocalExecutionDevice(id: Int, override val port: Int, override val address: InetAddress) : IncarnatedDevice(id), PhysicalDevice {

    override fun getSensor(sensorName: String): Any {
        communication.sendToDevice(Message(id, MessageType.ReadSensor, sensorName))
        return 0
    }

    override fun execute() = incarnation.execute()

    fun goFullWeight() = RemoteDevice(id, port, address)
}