package devices

import communication.Communication
import communication.Message
import communication.MessageType
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress

/**
 * Device model that does everything remotely
 */
class RemoteDevice(id: Int, override val port: Int, override val address: InetAddress) : AbstractDevice(id), PhysicalDevice {

    /*override fun getSensor(sensorName: String): Any {
        communication.sendToDevice(Message(id, MessageType.ReadSensor, sensorName))
        return 0
    }*/

    override fun execute() = communication.sendToDevice(Message(id, MessageType.Execute))

    fun goLightWeight() = LocalExecutionDevice(id, port, address).apply {
        //initialize(, )
    }
}