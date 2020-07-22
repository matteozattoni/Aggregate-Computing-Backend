package server.interfaces

import adapters.Adapter
import devices.implementations.SupportDevice
import devices.interfaces.EmulatedDevice
import devices.interfaces.RemoteDevice
import org.protelis.lang.datatype.DeviceUID
import server.Execution
import java.io.Serializable

/**
 * Specified an Abstract Factory pattern. Each one who uses this platform can give their own implementation of Remote
 * Device or Emulator, DeviceUID and even a Server Support, this open the possibility to extend almost the whole
 * platform.
 */
interface ServerFactory {

    fun createNewID(): DeviceUID
    fun createRemoteDevice(uid: DeviceUID): RemoteDevice
    fun createEmulatedDevice(uid: DeviceUID, name: String, adapter: (EmulatedDevice) -> Adapter = Execution.adapter,
                             onResult: (Serializable) -> Any = ::println): EmulatedDevice
    fun createSupport(): SupportDevice?
}