package devices.server

import adapters.Adapter
import devices.EmulatedDevice

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int, name: String = "", adapterBuilder: (EmulatedDevice) -> Adapter) : EmulatedDevice(id, name, adapterBuilder)