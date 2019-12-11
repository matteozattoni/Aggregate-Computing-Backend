package devices

import backend.Backend
import communication.Communication
import communication.Message
import communication.SocketCommunication
import java.net.InetAddress
import java.net.SocketAddress

class DummyDevice(id: Int) : AbstractDevice(id) {
    override fun execute() {

    }

    override fun tell(message: Message) {

    }
}