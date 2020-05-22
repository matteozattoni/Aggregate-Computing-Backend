package communication.interfaces

import devices.interfaces.RemoteDevice
import java.net.Socket

abstract class NetworkCommunication(override val device: RemoteDevice) :
    Communication<Socket> {

    // chain of responsibility pattern
    private var nextCommunication: NetworkCommunication? = null

    fun addCommunication(networkCommunication: NetworkCommunication) {
        if (nextCommunication == null) {
            this.nextCommunication = networkCommunication
        } else {
            this.nextCommunication!!.addCommunication(networkCommunication)
        }
    }

    abstract fun connect(onReceive: (Socket) -> Unit = ::serverCallback)

    abstract fun isConnectedToClient(): Boolean
}