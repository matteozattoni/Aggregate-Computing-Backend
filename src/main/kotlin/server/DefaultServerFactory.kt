package server

import adapters.Adapter
import adapters.protelis.ServerUID
import devices.implementations.PeerDevice
import devices.implementations.VirtualDevice
import devices.interfaces.EmulatedDevice
import devices.interfaces.RemoteDevice
import org.protelis.lang.datatype.DeviceUID
import server.interfaces.ServerFactory
import devices.implementations.SupportDevice
import java.io.Serializable

/**
 * This concrete factory is used if none is specified
 */
internal class DefaultServerFactory: ServerFactory {

    override fun createNewID(): DeviceUID {
        return ServerUID()
    }

    override fun createRemoteDevice(uid: DeviceUID): RemoteDevice {
        return PeerDevice(uid)
    }

    override fun createEmulatedDevice(
        uid: DeviceUID,
        name: String,
        adapter: (EmulatedDevice) -> Adapter,
        onResult: (Serializable) -> Any
    ): EmulatedDevice {
        return VirtualDevice(uid, name, adapter, onResult)
    }

    override fun createSupport(): SupportDevice? {
        val id = createNewID()
        return SupportDevice(id, factory = this)
    }
}