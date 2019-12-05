package incarnations.protelis

import backend.Backend
import communication.Message
import devices.IncarnatedDevice
import devices.PhysicalDevice
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

class EmulatedNetworkManager(private val uid: IntUID) : NetworkManager {
    private val device
    get () = Backend.devices.single { it.id == uid.getUID() }

    private val neighbours
    get() = Backend.getNeighbours(uid.getUID())

    @Suppress("UNCHECKED_CAST")
    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> {
        val messages = device.communication.received
        return messages.map { (IntUID(it.senderUid) as DeviceUID) to (it.content as Map<CodePath, Any>) }.toMap().apply { messages.clear() }
    }

    override fun shareState(toSend: Map<CodePath, Any>) {

        if (toSend.isNotEmpty()) {
            neighbours
                .map { it.communication }
                .forEach { device.communication.send(Message(uid.getUID(), toSend), it.address, it.port) }
        }
    }
}