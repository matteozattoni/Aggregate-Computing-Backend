package devices.implementations

import adapters.Adapter
import devices.interfaces.EmulatedDevice
import java.io.Serializable

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int, name: String = "",
                    adapterBuilder: (EmulatedDevice) -> Adapter,
                    onResult: (Serializable) -> Any = ::println) : EmulatedDevice(id, name, adapterBuilder, onResult)