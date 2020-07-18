package communication.concrete.localipv

import communication.Message
import communication.concrete.localipv.LocalClientCommunication
import devices.interfaces.RemoteDevice
import server.interfaces.NetworkInformation
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.Socket

class LocalNetworkInformation(
    val socket: Socket,
    val inputStream: ObjectInputStream,
    val outputStream: ObjectOutputStream,
    private val message: Message
) : NetworkInformation {

    lateinit var server: RemoteDevice

    override fun getContent(): Serializable? {
        return message
    }

    override fun setPhysicalDevice(peer: RemoteDevice) {
        val localCommunication = LocalClientCommunication(peer, this)
        localCommunication.server = this.server
        peer.setPhysicalDevices(localCommunication)
    }
}