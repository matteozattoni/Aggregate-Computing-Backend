package adapters.protelis

import communication.Message
import communication.MessageType
import controller.NetworkController
import devices.interfaces.Device
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

/**
 * Protelis wrapper for Devices Communication
 */
class ProtelisNetworkManager(private val device: Device,
                             private val server: Device = NetworkController.getNetworkController().support!!)
    : NetworkManager {

    /**
     * Receive the status from this Device's neighbours
     */
    @Suppress("UNCHECKED_CAST")
    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> {
        return device.status
            .map { it.senderUid to (it.content as Map<CodePath, Any>) }
            .toMap()
            .apply { device.status.clear() }
    }

    /**
     * Send the status of this Device to its neighbours
     */
    override fun shareState(toSend: Map<CodePath, Any>) {
        if (toSend.isNotEmpty()) {
            val message = Message(device.id, MessageType.Status, HashMap(toSend))

            server.tell(Message(device.id, MessageType.SendToNeighbours, message))
        }
    }
}