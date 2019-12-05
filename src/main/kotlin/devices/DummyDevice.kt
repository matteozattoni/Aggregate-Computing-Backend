package devices

import communication.SocketCommunication
import java.net.InetAddress

class DummyDevice(id: Int) : AbstractDevice(id, SocketCommunication(InetAddress.getLocalHost(), 20000)) {
    override fun execute() {

    }

    override fun getSensor(sensorName: String): Any {
        return 0
    }
}