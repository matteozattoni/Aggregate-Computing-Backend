package devices

import backend.Backend
import communication.SocketCommunication
import java.net.InetAddress

class DummyDevice(id: Int) : AbstractDevice(id) {
    override fun execute() {

    }
}