package communication.interfaces

import communication.Message
import devices.interfaces.RemoteDevice
import devices.implementations.SupportDevice

interface CommunicationController {
    var support: SupportDevice?
    fun getRemoteServer(onServerReady: (RemoteDevice) -> Unit, onMessage: (Message) -> Unit)
    fun startOfferServer()
    fun stopOfferServer()
    fun setCommunicationForServer(server: RemoteDevice)
}