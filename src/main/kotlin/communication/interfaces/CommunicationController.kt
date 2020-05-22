package communication.interfaces

import communication.Message
import devices.interfaces.RemoteDevice

interface CommunicationController {
    var support: RemoteDevice?
    fun getRemoteServer(onServerReady: (RemoteDevice) -> Unit, onMessage: (Message) -> Unit)
    fun startOfferServer()
    fun stopOfferServer()
    fun setCommunicationForServer(server: RemoteDevice)
}