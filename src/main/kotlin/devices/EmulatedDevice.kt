package devices

import communication.Communication
import communication.Message
import communication.SocketCommunication
import incarnations.Incarnation
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress

/**
 * Fully emulated Device
 */
class EmulatedDevice(id: Int, incarnationBuilder: (EmulatedDevice) -> Incarnation) : IncarnatedDevice(id) {
    override val incarnation: Incarnation = incarnationBuilder(this)

    override fun getSensor(sensorName: String): Any = incarnation.readSensor(sensorName)

    override fun execute() = incarnation.execute()

    override fun tell(message: Message) {
        receivedMessages.add(message)
    }
}