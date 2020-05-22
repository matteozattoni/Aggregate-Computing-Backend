package server.interfaces

import adapters.Adapter
import devices.interfaces.EmulatedDevice
import devices.interfaces.RemoteDevice
import org.protelis.lang.datatype.DeviceUID
import server.Execution
import java.io.Serializable

interface ServerFactory {

    fun createNewID(): DeviceUID
    fun createRemoteDevice(uid: DeviceUID): RemoteDevice
    fun createEmulatedDevice(uid: DeviceUID, name: String, adapter: (EmulatedDevice) -> Adapter = Execution.adapter,
                             onResult: (Serializable) -> Any = ::println): EmulatedDevice

}