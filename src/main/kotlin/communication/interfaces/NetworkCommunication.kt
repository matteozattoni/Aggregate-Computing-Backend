package communication.interfaces

import devices.interfaces.RemoteDevice
import java.net.Socket

/**
 * Represent how the [RemoteDevice] communicates. Extends [Communication], here the T
 * parameter is a [Socket], moreover is using a Chain of Responsibility pattern where more [NetworkCommunication] can add
 * Network Protocol to reach the device.
 */
abstract class NetworkCommunication(override val device: RemoteDevice) :
    Communication<Socket> {

    // chain of responsibility pattern
    private var nextCommunication: NetworkCommunication? = null

    /**
     * Adds a [networkCommunication] to the Chain of Responsibility pattern
     */

    fun addCommunication(networkCommunication: NetworkCommunication) {
        if (nextCommunication == null) {
            this.nextCommunication = networkCommunication
        } else {
            this.nextCommunication!!.addCommunication(networkCommunication)
        }
    }

    /**
     * This is called for starting the connection
     * @param onReceive this callback is called when the socket is connected and data can be sent
     */
    abstract fun connect(onReceive: (Socket) -> Unit = ::serverCallback)

    abstract fun isConnectedToClient(): Boolean
}