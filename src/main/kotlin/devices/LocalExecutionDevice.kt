package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import java.net.SocketAddress

/**
 * Device model that executes locally but reads/writes sensors/actuators remotely
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress, adapterBuilder: (LocalExecutionDevice) -> Adapter) : EmulatedDevice(id), InternetDevice {
    override val communication = SocketCommunication(this)
    override val adapter: Adapter = adapterBuilder(this)

    override fun getSensor(sensorName: String): Any {
        communication.send(Message(id, MessageType.ReadSensor, sensorName))
        return 0
    }

    override fun execute() = adapter.execute()

    override fun tell(message: Message) {

    }

    fun goFullWeight() = RemoteDevice(id, address)
}