package devices

import communication.Communication
import communication.Message
import communication.MessageType
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress
import java.net.SocketAddress

/**
 * Device model that executes locally but reads/writes sensors/actuators remotely
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress, incarnationBuilder: (LocalExecutionDevice) -> Incarnation) : IncarnatedDevice(id), InternetDevice {
    override val communication = SocketCommunication(this)
    override val incarnation: Incarnation = incarnationBuilder(this)

    override fun getSensor(sensorName: String): Any {
        communication.send(Message(id, MessageType.ReadSensor, sensorName))
        return 0
    }

    override fun execute() = incarnation.execute()

    override fun tell(message: Message) {

    }

    fun goFullWeight() = RemoteDevice(id, address)
}