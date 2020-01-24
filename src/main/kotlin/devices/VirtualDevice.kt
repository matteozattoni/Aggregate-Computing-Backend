package devices

import adapters.Adapter

/**
 * Fully emulated Device
 */
class VirtualDevice(id: Int, name: String = "", adapterBuilder: (EmulatedDevice) -> Adapter) : EmulatedDevice(id, name, adapterBuilder)