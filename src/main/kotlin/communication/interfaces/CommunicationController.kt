package communication.interfaces

import communication.Message
import devices.interfaces.RemoteDevice
import devices.implementations.SupportDevice

/**
 * Represents the way a Device model has to communicate with the physical one.
 * This interfaces represent the responsibility that have each controller, this controller can be added to
 * NetworkController.
 */

interface CommunicationController {
    var support: SupportDevice?

    /**
     * This is called by the NetworkController to reach the SupportDevice and get message from it, the NetworkController informs the Client through the callbacks
     * @param onServerReady this callback is called when the SupportDevice is found and a join has been sent to it,
     * @param onMessage this callback is called when messages have been received from SupportDevice
     */
    fun getRemoteServer(onServerReady: (RemoteDevice) -> Unit, onMessage: (Message) -> Unit)

    /**
     * Start the service to offer SupportDevice
     */
    fun startOfferServer()

    /**
     * Stop the service to offer SupportDevice, nobody can discover the SupportDevice anymore
     */
    fun stopOfferServer()

    /**
     * Set the communication to the server
     * @param server the SupportDevice (it uses the interface RemoteDevice instead of SupportDevice for more abstraction)
     */
    fun setCommunicationForServer(server: RemoteDevice)
}