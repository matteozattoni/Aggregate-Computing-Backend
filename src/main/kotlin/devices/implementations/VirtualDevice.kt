package devices.implementations

import adapters.Adapter
import devices.interfaces.EmulatedDevice

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int, name: String = "",
                    adapterBuilder: (EmulatedDevice) -> Adapter,
                    onResult: (String) -> Any = ::println) : EmulatedDevice(id, name, adapterBuilder, onResult)