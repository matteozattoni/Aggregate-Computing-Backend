package incarnations.protelis

import backend.Backend
import communication.Message
import communication.MessageType
import devices.Device
import devices.IncarnatedDevice
import devices.PhysicalDevice
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

/**
 * Protelis wrapper for Devices Communication
 *
 */
class EmulatedNetworkManager(private val device: Device) : NetworkManager {
    private val neighbours
    get() = Backend.getNeighbours(device)

    /**
     * Receive the state from this Device's neighbours
     */
    @Suppress("UNCHECKED_CAST")
    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> {
        return device.communication.received
            .map { (IntUID(it.senderUid) as DeviceUID) to (it.content as Map<CodePath, Any>) }
            .toMap()
            .apply { device.communication.received.clear() }
    }

    /**
     * Send the state of this Device to its neighbours
     */
    override fun shareState(toSend: Map<CodePath, Any>) {
        if (toSend.isNotEmpty()) {
            neighbours
                .filterIsInstance<PhysicalDevice>()
                .forEach { device.communication.send(Message(device.id, MessageType.Result, toSend), it.address, it.port) }
        }
    }
}