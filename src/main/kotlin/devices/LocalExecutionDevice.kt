package devices

import communication.Message
import communication.MessageType
import communication.SocketCommunication
import adapters.Adapter
import java.net.SocketAddress

/**
 * Device model that executes locally but reads/writes sensors/actuators remotely
 */
class LocalExecutionDevice(id: Int, override val address: SocketAddress) : EmulatedDevice(id), InternetDevice {
    override val physicalDevice = SocketCommunication(this)

    override fun getSensor(sensorName: String): Any {
        physicalDevice.send(Message(id, MessageType.ReadSensor, sensorName))
        return 0
    }

    override fun execute() = adapter!!.execute()

    fun goFullWeight() = RemoteDevice(id, address)
}