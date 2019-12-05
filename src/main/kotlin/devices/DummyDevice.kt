package devices

import backend.Backend
import communication.SocketCommunication
import java.net.InetAddress

class DummyDevice(id: Int) : AbstractDevice(id, SocketCommunication(InetAddress.getLocalHost(), Backend.communication.port + id + 1)) {
    override fun execute() {

    }

    override fun getSensor(sensorName: String): Any {
        return 0
    }
}