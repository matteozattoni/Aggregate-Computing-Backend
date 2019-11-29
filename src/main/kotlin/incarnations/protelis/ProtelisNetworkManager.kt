package incarnations.protelis

import backend.Backend
import devices.IncarnatedDevice
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

class ProtelisNetworkManager(private val uid: DeviceUID) : NetworkManager {
    private var messages: Map<DeviceUID, Map<CodePath, Any>> = emptyMap()

    private val neighbours
    get() = Backend.getNeighbours((uid as IntUID).getUID())

    private fun receiveMessage(src: DeviceUID, msg: Map<CodePath, Any>) {
        messages += Pair(src, msg)
    }

    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> = messages.apply { messages = emptyMap() }

    override fun shareState(toSend: Map<CodePath, Any>) {
        if (toSend.isNotEmpty()) {
            neighbours
                .filterIsInstance<IncarnatedDevice>()
                .map { it.incarnation }
                .filterIsInstance<ProtelisIncarnation>()
                .forEach { it.networkManager.receiveMessage(uid, toSend) }
        }
    }
}