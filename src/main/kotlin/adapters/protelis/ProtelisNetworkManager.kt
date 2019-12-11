package adapters.protelis

import server.Support
import communication.Message
import communication.MessageType
import devices.Device
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

/**
 * Protelis wrapper for Devices Communication
 *
 */
class ProtelisNetworkManager(private val device: Device) : NetworkManager {
    private val neighbours
    get() = Support.getNeighbours(device)

    /**
     * Receive the state from this Device's neighbours
     */
    @Suppress("UNCHECKED_CAST")
    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> {
        return device.receivedMessages
            .map { (IntUID(it.senderUid) as DeviceUID) to (it.content as Map<CodePath, Any>) }
            .toMap()
            .apply { device.receivedMessages.clear() }
    }

    /**
     * Send the state of this Device to its neighbours
     */
    override fun shareState(toSend: Map<CodePath, Any>) {
        if (toSend.isNotEmpty()) {
            neighbours.forEach { it.tell(Message(device.id, MessageType.Result, toSend)) }
        }
    }
}